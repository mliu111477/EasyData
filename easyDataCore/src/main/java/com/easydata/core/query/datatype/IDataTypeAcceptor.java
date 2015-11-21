package com.easydata.core.query.datatype;

import com.easydata.core.query.Condition;

/**
 * 数据有效性验证器。如果使用smartSearch的时候，则会通过该类的具体实现类来进行数据有效性验证。<br />
 *
 * @author Mr.Pro
 */
public interface IDataTypeAcceptor {

	/**
	 * 
	 * 标题: accept <br />
	 * 描述: 进行数据有效性验证 
	 * @param type 数据类型
	 * @param value 该数据的值
	 * @param condition 该条件
	 * @return 如果有效的话则返回该对象的值，否则返回null
	 */
	public Object accept(DataType type, Object value, Condition condition);
}