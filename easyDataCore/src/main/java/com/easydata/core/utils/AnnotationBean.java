package com.easydata.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easydata.core.annotation.Id;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 注解类的参数信息。<br />
 *
 * @author Mr.Pro
 */
public class AnnotationBean {

	// 表名
	private String tableName;
	private Class<?> beanClass;

	// id相关属性
	private List<AnnotationIdFieldToNameEntry> idFields;
	private List<Object> idFiledValues;

	// 其余属性
	private Map<String, Object> otherFields = new HashMap<String, Object>();

	// 其可用的属性名称(otherFields中的keys)
	private List<AnnotationFieldToNameEntry> fieldNames = new ArrayList<AnnotationFieldToNameEntry>();
	// 其不可用的属性名称(otherFields中的keys)
	private List<AnnotationFieldToNameEntry> disableFieldNames = new ArrayList<AnnotationFieldToNameEntry>();
	
	// 是否包含id属性
	private boolean hasIdField;

	// 是否已经对齐进行赋值了
	private boolean hasSetValues;
	
	private static final Log logger = LogFactory.getLog(AnnotationBean.class);

	/**
	 * 判断在指定的非ID所有域中是否包含指定数据类型的数据
	 * @param cls
	 * @return
	 */
	public boolean containFieldType(Class<?> cls) {
		for (AnnotationFieldToNameEntry fieldName : fieldNames) {
			if (fieldName.getField().getType() == cls) {
				return true;
			}
		}
		return false;
	}

	public boolean getHasIdField() {
		return hasIdField;
	}

	public void setHasIdField(boolean hasIdField) {
		this.hasIdField = hasIdField;
	}

	public List<Object> getIdFiledValues() {
		return idFiledValues;
	}

	public void setIdFiledValues(List<Object> idFiledValues) {
		this.idFiledValues = idFiledValues;
	}

	public boolean isHasIdField() {
		return hasIdField;
	}

	public Map<String, Object> getOtherFields() {
		return otherFields;
	}

	public void setOtherFields(Map<String, Object> otherFields) {
		this.otherFields = otherFields;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<AnnotationIdFieldToNameEntry> getIdFields() {
		return idFields;
	}

	public void setIdFields(List<AnnotationIdFieldToNameEntry> idFields) {
		this.idFields = idFields;
	}

	public List<AnnotationFieldToNameEntry> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<AnnotationFieldToNameEntry> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public List<AnnotationFieldToNameEntry> getDisableFieldNames() {
		return disableFieldNames;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public void setDisableFieldNames(List<AnnotationFieldToNameEntry> disableFieldNames) {
		this.disableFieldNames = disableFieldNames;
	}

	public boolean isHasSetValues() {
		return hasSetValues;
	}

	public void setHasSetValues(boolean hasSetValues) {
		this.hasSetValues = hasSetValues;
	}

	public String toIdString(){
		if (!hasSetValues)
			return null;

		if (!hasIdField) {
			logger.error(new StringBuilder().append("当前类(").append(beanClass).append(")中不存在任何ID数据信息，请确认该类是否存在ID注解信息！"));
			return null;
		}

		StringBuilder idString = new StringBuilder();
		int eachCount = Math.min(idFields.size(), idFiledValues.size());
		for (int i = 0; i < eachCount; i++) {
			AnnotationIdFieldToNameEntry entry = idFields.get(i);
			Id idAnnotation = entry.getId();
			idString.append(idAnnotation.prefix()).append(idFiledValues.get(i));
		}

		return idString.toString();
	}

	/**
	 * 转换喂
	 * @return
	 */
	public String toJSONString(boolean containIdFailds){
		JSONObject object = toJSONObject(containIdFailds);
		return object == null ? null : object.toJSONString();
	}

	public JSONObject toJSONObject(boolean containIdFields){
		if (!hasSetValues) {
			return null;
		}

		JSONObject object = new JSONObject();

		// 设置Id值
		if (containIdFields && hasIdField) {
			if (idFields != null && idFiledValues != null) {
				int eachCount = Math.min(idFields.size(), idFiledValues.size());
				for (int i = 0; i < eachCount; i++) {
					object.put(idFields.get(i).getUseName(), idFiledValues.get(i));
				}
			}
		}

		if (otherFields != null && otherFields.size() > 0) {
			for (Map.Entry<String, Object> entry : otherFields.entrySet()) {
				String useName = entry.getKey();
				Object objValue = entry.getValue();
				if (objValue instanceof AnnotationBean) {
					object.put(useName, ((AnnotationBean)objValue).toJSONObject(true));
				} else {
					object.put(useName, objValue);
				}
			}
		}

		return object;
	}

	public <T> T parseData(String jsonData) throws Exception {
		JSONObject object = JSON.parseObject(jsonData);
		return parseData(object);
	}

	public <T> T parseData(JSONObject jsonObject) throws Exception {
		// 先进性实例化的操作
		T data = (T) beanClass.newInstance();

		// 设置ID
		if (hasIdField && idFields != null && idFields.size() > 0) {
			for (AnnotationIdFieldToNameEntry idField : idFields) {
				Object jsonVal = jsonObject.get(idField.getUseName());
				idField.getField().set(data, jsonVal);
			}
		}

		// 设置其他属性
		if (fieldNames != null && fieldNames.size() > 0) {
			for (AnnotationFieldToNameEntry fieldToNameEntry : fieldNames) {

				if (fieldToNameEntry.isBasicType()) {
					Object jsonVal = jsonObject.get(fieldToNameEntry.getUseName());
					fieldToNameEntry.getField().set(data, jsonVal);
				} else {
					JSONObject jsonVal = jsonObject.getJSONObject(fieldToNameEntry.getUseName());
					if (jsonVal == null) continue;
					Class<?> beanClass = fieldToNameEntry.getField().getType();
					AnnotationBean propBean = AnnotationBeanUtil.parseBasicBeanByClass(beanClass);
					fieldToNameEntry.getField().set(data, propBean.parseData(jsonVal));
				}
			}
		}
		return data;
	}

	public <T> List<T> parseData(JSONArray jsonArray) throws Exception {
		if (jsonArray == null)
			return null;
		if (jsonArray.size() == 0)
			return new ArrayList<T>(0);

		List<T> list = new ArrayList<T>(jsonArray.size());
		for (Object o : jsonArray) {
			list.add(this.<T>parseData((JSONObject) o));
		}
		return list;
	}

	public static void main(String[] args) {
		List<String> list = Arrays.asList("1", "2", "3", "4");
		JSONArray array = new JSONArray();
		array.addAll(list);
		System.out.println(array.toJSONString());

	}
}
