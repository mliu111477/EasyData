package com.easydata.core.query.datatype;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * 数据类型。<br />
 *
 * @author Mr.Pro
 */
public enum DataType {

	/**
	 * 文本型
	 */
	STRING,
	/**
	 * 整数型
	 */
	INT,
	/**
	 * 长整形
	 */
	LONG,
	/**
	 * 单浮点型
	 */
	FLOAT,
	/**
	 * 双浮点型
	 */
	DOUBLE,
	/**
	 * 布尔型
	 */
	BOOLEAN,
	/**
	 * 日期类型
	 */
	DATE;
	
	public static DataType parseDataTypeFromClass(Class cls) {
		if (cls == null) {
			return null;
		}

		if (cls == String.class) {
			return STRING;
		} else if (cls == Integer.class || cls == Integer.TYPE) {
			return INT;
		} else if (cls == Long.class || cls == Long.TYPE) {
			return LONG;
		} else if (cls == Float.class || cls == Float.TYPE) {
			return FLOAT;
		} else if (cls == Double.class || cls == Double.TYPE) {
			return DOUBLE;
		} else if (cls == Boolean.class || cls == Boolean.TYPE) {
			return BOOLEAN;
		} else if (cls == Timestamp.class || cls == Date.class || cls == java.sql.Date.class || cls == Calendar.class) {
			return DATE;
		}
		return null;
	}
}

































