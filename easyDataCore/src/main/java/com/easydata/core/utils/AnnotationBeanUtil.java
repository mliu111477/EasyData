package com.easydata.core.utils;

import com.easydata.core.annotation.DataBean;
import com.easydata.core.annotation.Id;
import com.easydata.core.annotation.Transient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 注解Bean的工具类。<br />
 *
 * @author Mr.Pro
 */
public class AnnotationBeanUtil {

	// 类和域的缓存对象
	private static Map<Class<?>, List<Field>> CLASS_TO_FIELDS_CACHE = new HashMap<Class<?>, List<Field>>();
	// 类与其属性信息的缓存(在bean中主要缓存器)
	private static Map<Class<?>, AnnotationBean> CLASS_TO_BEAN_CACHE = new HashMap<Class<?>, AnnotationBean>();

    private static List<Class<?>> IGNORE_BEANS = new ArrayList<Class<?>>();

	// 日志记录
	private static Log logger = LogFactory.getLog(AnnotationBeanUtil.class);

    private static AnnotationBean doRealParse(Object object, AnnotationBean bean, List<Class<?>> linkedClasses) {

        // 获取所有关于id的数据信息
        List<Object> idFieldValues = new ArrayList<Object>(bean.getIdFields().size());
        for (AnnotationIdFieldToNameEntry idFieldToNameEntry : bean.getIdFields()) {
            idFieldValues.add(getValueFormField(idFieldToNameEntry.getField(), object));
        }
        bean.setIdFiledValues(idFieldValues);

        // 获取其他与之相关的数据信息
        Map<String, Object> otherFields = new HashMap<String, Object>(bean.getFieldNames().size());
        for (AnnotationFieldToNameEntry fieldToNameEntry : bean.getFieldNames()) {
			boolean shouldContinue = false;
            if (linkedClasses.size() > 0) {
                for (Class<?> linkedClass : linkedClasses) {
                    if (fieldToNameEntry.getField().getType() == linkedClass) {
						shouldContinue = true;
						logger.warn(new StringBuilder().append("由于指定的类(").append(fieldToNameEntry.getField().getType()).append(")中存在类(").append(object.getClass()).append(")的引用，导致循环引用。所以直接将其属性过滤。"));
                        break;
                    }
                }
            }

			if (shouldContinue) {
				continue;
			}
            if (fieldToNameEntry.isBasicType() || fieldToNameEntry.isDateType()) {
                otherFields.put(fieldToNameEntry.getUseName(), getValueFormField(fieldToNameEntry.getField(), object));
            } else {
                linkedClasses.add(object.getClass());
				Object beanVaue = getValueFormField(fieldToNameEntry.getField(), object);
				if (beanVaue == null) continue;

				AnnotationBean basicBeanValue = parseBasicBeanByClass(beanVaue.getClass());
                otherFields.put(fieldToNameEntry.getUseName(), doRealParse(beanVaue, basicBeanValue, linkedClasses));
            }
        }
        bean.setOtherFields(otherFields);

		bean.setHasSetValues(true);
        return bean;
    }

	/**
	 *
	 * 标题: parseBean <br />
	 * 描述: 将指定的对象转换为注解Bean并且将其中的值放入Bean中
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static AnnotationBean parseBean(Object object) throws IllegalArgumentException{
		if(object == null)
			return null;

		// 获取所有的域(包括父类的)
		List<Field> fields = getAllFieldsByObjectClassAndSetAccess(object.getClass());

		if(fields.size() == 0)
			throw new IllegalArgumentException("该对象"+object.getClass()+"中并不存在任何域。");

        AnnotationBean bean = parseBasicBeanByClass(object.getClass());
        if (bean == null) {
            return null;
        }

        return doRealParse(object, bean, new ArrayList<Class<?>>());
	}


	/**
	 *
	 * 标题: parseBasicBeanByClass <br />
	 * 描述: 将指定的class转换为基础的bean对象
	 * @param cls class对象
	 * @return 注解bean对象
	 */
	public static AnnotationBean parseBasicBeanByClass(Class<?> cls){
		AnnotationBean bean = null;
		if((bean = CLASS_TO_BEAN_CACHE.get(cls)) != null){
			AnnotationBean ret = new AnnotationBean();
            ret.setTableName(bean.getTableName());
			ret.setBeanClass(bean.getBeanClass());
			ret.setIdFields(bean.getIdFields());
			ret.setFieldNames(bean.getFieldNames());
			ret.setDisableFieldNames(bean.getDisableFieldNames());
			ret.setHasIdField(bean.getHasIdField());
			return ret;
		}

        // 如果当前的Class已经被认定为无法解析了，所以直接返回失败
        if (IGNORE_BEANS.contains(cls)) {
            return null;
        }

		bean = new AnnotationBean();
        bean.setBeanClass(cls);
		List<Field> fields = getAllFieldsByObjectClassAndSetAccess(cls);
		// 获取其中每个域的名称(除去id)
		List<AnnotationFieldToNameEntry> fieldNames = new ArrayList<AnnotationFieldToNameEntry>(fields.size());
		List<AnnotationFieldToNameEntry> disableFieldNames = new ArrayList<AnnotationFieldToNameEntry>(fields.size());

		List<AnnotationIdFieldToNameEntry> idNames = new ArrayList<AnnotationIdFieldToNameEntry>(fields.size());

		// 遍历所有的域
		Id id = null;
		com.easydata.core.annotation.Field fieldAnno = null;

		// 判断该类是否包含Table的注解
        String willSetName = cls.getSimpleName();
		try {
			DataBean tableAnno = cls.getAnnotation(DataBean.class);
			if(tableAnno != null && StringUtil.isNotEmpty(tableAnno.value())){
				willSetName = tableAnno.value();
			}
		} catch (Exception e) {
			logger.error("未能成功获取该类的Table注解", e);
		}
        bean.setTableName(willSetName);

		for(Iterator<Field> it = fields.iterator(); it.hasNext();){
			Field field = it.next();

			// 如果拥有id注解
			if((id = field.getAnnotation(Id.class)) != null){
				AnnotationIdFieldToNameEntry idField = new AnnotationIdFieldToNameEntry(id, field, StringUtil.isEmpty(id.value(), field.getName()));
                if (!idField.isBasicType()) {
                    logger.error(new StringBuilder().append("未能成功对指定的数据类型(").append(cls.getName()).append(")进行解析，该类中的字段(").append(field).append(")的数据类型不是基础数据类型，无法进行处理！"));
                    IGNORE_BEANS.add(cls);
                    return null;
                }
				idNames.add(idField);
				bean.setHasIdField(true);
				continue;
			}

			// 如果拥有field注解
			if(null != (fieldAnno = field.getAnnotation(com.easydata.core.annotation.Field.class))){
				String useName = StringUtil.isEmpty(fieldAnno.value(), field.getName());
				fieldNames.add(new AnnotationFieldToNameEntry(field, useName));
				continue;
			}

			// 如果没有设置transient注解，则说明增加到其他属性的映射中
			if(field.getAnnotation(Transient.class) == null){
				fieldNames.add(new AnnotationFieldToNameEntry(field, field.getName()));
			}else{
				disableFieldNames.add(new AnnotationFieldToNameEntry(field, field.getName()));
			}
		}
		bean.setFieldNames(fieldNames);
		bean.setDisableFieldNames(disableFieldNames);

        // 对id列表进行排序操作
        int idCount = idNames.size();
        List<AnnotationIdFieldToNameEntry> willIdNames = new ArrayList<AnnotationIdFieldToNameEntry>(idCount);
        if (idCount > 0) {
            if (idCount == 1) {
                willIdNames.add(idNames.get(0));
            } else {
				// 如果id的数量大于1的话，则需要按照自定义的顺序进行排序，如果order属性相同的话则会按照字母顺序排序
				SortedMap<Integer, List<AnnotationIdFieldToNameEntry>> willSortedIds = new TreeMap<Integer, List<AnnotationIdFieldToNameEntry>>();
				for (AnnotationIdFieldToNameEntry idFieldToNameEntry : idNames) {
					int order = idFieldToNameEntry.getId().order();
					List<AnnotationIdFieldToNameEntry> entryList = CollectionUtil.get(willSortedIds, order, new ArrayList<AnnotationIdFieldToNameEntry>());
					entryList.add(idFieldToNameEntry);
					willSortedIds.put(order, entryList);
				}

				for (List<AnnotationIdFieldToNameEntry> entryList : willSortedIds.values()) {
					if (entryList.size() == 1) {
						willIdNames.add(entryList.get(0));
					} else {
						Collections.sort(entryList, new Comparator<AnnotationIdFieldToNameEntry>() {
							public int compare(AnnotationIdFieldToNameEntry t1, AnnotationIdFieldToNameEntry t2) {
								return t1.getUseName().compareTo(t2.getUseName());
							}
						});
						willIdNames.addAll(entryList);
					}
				}
			}

        }
        bean.setIdFields(willIdNames);

		CLASS_TO_BEAN_CACHE.put(cls, bean);
		return bean;
	}
//
//	/**
//	 *
//	 * 标题: getIdFieldNameByClass <br />
//	 * 描述: 根据Class文件获取其id名称
//	 * @param cls class文件
//	 * @return id名称，如果不存在主键注解信息，则返回null
//	 */
//	public static String getIdFieldNameByClass(Class<?> cls) {
//		// 获取所有的域(包括父类的)
//		try {
//			AnnotationBean bean = parseBasicBeanByClass(cls);
//			return bean.getIdField().getUseName();
//		} catch (Exception e) {
//			logger.error("该类【"+cls+"】尚未制定主键注解，请确认信息", e);
//			return null;
//		}
//	}

	/**
	 *
	 * 标题: getAllFieldsByObjectClassAndSetAccess <br />
	 * 描述: 获得该类的所有域(包括父类)，并设置其为可访问的
	 * @param cls 需要进行解析的class
	 * @return 该类的所有域
	 */
	public static List<Field> getAllFieldsByObjectClassAndSetAccess(Class<?> cls){
		List<Field> fields = null;
		if((fields = CLASS_TO_FIELDS_CACHE.get(cls)) != null)
			return fields;

		fields = new ArrayList<Field>(Arrays.asList(cls.getDeclaredFields()));
		if(cls.getSuperclass() != Object.class)
			fields.addAll(getAllFieldsByObjectClassAndSetAccess(cls.getSuperclass()));
		for(Field field : fields){
			field.setAccessible(true);
		}
		CLASS_TO_FIELDS_CACHE.put(cls, fields);
		return fields;
	}

	/**
	 *
	 * 标题: getValueFormField <br />
	 * 描述: 从field对象中获得值
	 * @param field 域对象
	 * @param obj 对象
	 * @return
	 */
	private static Object getValueFormField(Field field, Object obj) {
		try {
			return field.get(obj);
		} catch (Exception e) {
			logger.warn("未能成功获取"+field+"的值", e);
		}
		return null;
	}

	public static class A {
		private String name;
		private B b;

		public A() {
		}

		public A(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}
	}

	public static class B {
		@Id
		private int age;
		@Id
		private String id;
		private A a;

		public B() {
		}

		public B(int age, String id, A a) {
			this.age = age;
			this.id = id;
			this.a = a;
		}

		public void setA(A a) {
			this.a = a;
		}

		public int getAge() {

			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

	public static void main(String[] args) throws Exception {
		B b = new B(20, "idididid", new A("aaa"));
		AnnotationBean bean = parseBean(b);
//		System.out.println(bean.toJSONString(false));
		String jsonString = bean.toJSONString(true);

		b = bean.parseData(jsonString);
		System.out.println(b);
	}

//	/**
//	 *
//	 * 标题: setAllTransintFieldToNull <br />
//	 * 描述: 设置所有临时的域为空(默认值)
//	 * @param obj 设置对象
//	 */
//	public static Object setAllTransintFieldToNull(Object obj){
//		AnnotationBean bean = parseBasicBeanByClass(obj.getClass());
//		if(bean != null && bean.getDisableFieldNames().size() > 0){
//			for(AnnotationBasicFieldToNameEntry entry : bean.getDisableFieldNames()){
//				try {
//					entry.getField().set(obj, null);
//				} catch (Exception e) {
//					// 如果抛出异常，所以可能是基础数据类型
//					Field field = entry.getField();
//					Class<?> fieldType = field.getType();
//					try {
//						if(Integer.TYPE == fieldType || Short.TYPE == fieldType){
//							field.set(obj, 0);
//						}else if(Double.TYPE == fieldType || Float.TYPE == fieldType){
//							field.set(obj, 0.0);
//						}else if(Long.TYPE == fieldType){
//							field.set(obj, 0L);
//						}else if(Boolean.TYPE == fieldType){
//							field.set(obj, false);
//						}
//					} catch (Exception e1) {
//					}
//				}
//			}
//		}
//		return obj;
//	}
}
