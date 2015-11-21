package com.easydata.core.query;

/**
 * 搜索条件类型。<br />
 *
 * @author Mr.Pro
 */
public enum ConditionType {

	/**
	 * 相等
	 */
	EQUALS,
	/**
	 * 不相等
	 */
	NOT_EQUALS,
	/**
	 * 小于
	 */
	LT,
	/**
	 * 小于等于
	 */
	LT_EQUALS,
	/**
	 * 大于
	 */
	GT,
	/**
	 * 大于等于
	 */
	GT_EQUALS,
	/**
	 * 在...里
	 */
	IN,
	/**
	 * 不在...里
	 */
	NOT_IN,
	/**
	 * 像...，比如:%x%
	 */
	LIKE,
	/**
	 * 前缀匹配
	 */
	PRIFIX_MATCH,
	/**
	 * 后缀匹配
	 */
	SUFFIX_MATCH;
}
