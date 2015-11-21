package com.easydata.core.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 注解实体中名称和真正使用名称的映射。<br />
 *
 * @author Mr.Pro
 */
public class AnnotationBasicFieldToNameEntry {

	private Field field;
	private String useName;
	private Class<?> typeClass;
	private Boolean isBasicType;
	private Boolean isDateType;

	private int i;

	public AnnotationBasicFieldToNameEntry(Field field, String useName) {
		this.field = field;
		this.useName = useName;
		this.typeClass = field.getType();
	}

	/**
	 * 数据类型书否为基础数据类型或者String类型
	 * @return
	 */
	public boolean isBasicType(){
		if (isBasicType == null) {
			isBasicType = typeClass == String.class || Number.class.isAssignableFrom(typeClass)
					|| typeClass == Byte.TYPE || typeClass == Short.TYPE || typeClass == Integer.TYPE
					|| typeClass == Long.TYPE || typeClass == Character.TYPE || typeClass == Boolean.TYPE
					|| typeClass == Float.TYPE || typeClass == Double.TYPE;
		}
		return isBasicType;
	}

	/**
	 * 数据类型是否为日期类型
	 * @return
	 */
	public boolean isDateType(){
		if (isDateType == null) {
			isDateType = typeClass == java.util.Date.class || typeClass == java.sql.Date.class || typeClass == Calendar.class ||
					typeClass == Timestamp.class;
		}
		return isDateType;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	public static void main(String[] args) throws NoSuchFieldException {
		Field field = AnnotationBasicFieldToNameEntry.class.getDeclaredField("i");
//		System.out.println(field);
		System.out.println(Number.class.isAssignableFrom(field.getType()));
	}
}
