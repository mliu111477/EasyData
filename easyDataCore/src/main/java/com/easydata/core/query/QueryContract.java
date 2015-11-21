package com.easydata.core.query;

import com.easydata.core.query.datatype.IDataTypeAcceptor;
import com.easydata.core.query.datatype.impl.DefaultDataTypeAcceptor;

/**
 * 用于定义搜索组件中的一些常量。<br />
 *
 * @author Mr.Pro
 * @Time 15/11/13 下午4:24
 */
public class QueryContract {

	/**
	 * 默认的数据类型验证器
	 */
	public static IDataTypeAcceptor DEFAULT_DATA_ACCEPTOR = new DefaultDataTypeAcceptor();
}
