package com.easydata.core.query.datatype.impl;

import com.easydata.core.query.Condition;
import com.easydata.core.query.datatype.DataType;
import com.easydata.core.query.datatype.IDataTypeAcceptor;
import com.easydata.core.utils.DateUtil;
import com.easydata.core.utils.StringUtil;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 用于默认提供数据有效性验证器。<br />
 *
 * @author Mr.Pro
 */
public class DefaultDataTypeAcceptor implements IDataTypeAcceptor {

	public Object accept(DataType type, Object value, Condition condition) {
		switch (type) {
		case STRING:
			return value == null ? null : (StringUtil.isEmpty(value.toString()) ? null : value.toString());
		case INT:
			try {
				return Integer.parseInt(value.toString());
			} catch (Exception e) {
				return null;
			}
		case LONG:
			try {
				return Long.parseLong(value.toString());
			} catch (Exception e) {
				return null;
			}
		case FLOAT:
			try {
				Float.parseFloat(value.toString());
			} catch (Exception e) {
				return null;
			}
		case DOUBLE:
			try {
				Double.parseDouble(value.toString());
			} catch (Exception e) {
				return null;
			}
		case BOOLEAN:
			try {
				Boolean.parseBoolean(value.toString());
			} catch (Exception e) {
				return null;
			}
		case DATE:
			if(value == null)
				return null;
			if (value instanceof java.util.Date || value instanceof java.sql.Date)
				return value;
			else if (value instanceof Calendar)
				return ((Calendar) value).getTime();
			else if (value instanceof Timestamp)
				return new java.util.Date(((Timestamp) value).getTime());
			else {
				try {
					DateUtil.stringToDate(value.toString(), condition.getCustom().toString());
				} catch (Exception e) {
					return null;
				}
			}
		default:
			break;
		}
		return null;
	}

}
