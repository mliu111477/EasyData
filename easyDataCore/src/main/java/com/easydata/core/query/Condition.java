package com.easydata.core.query;

import com.easydata.core.query.datatype.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 每一个搜索条件的表达式。<br />
 *
 * @author Mr.Pro
 */
public class Condition {

	/**
	 * 字段名称
	 */
	private String fieldName;
	
	/**
	 * 字段值
	 */
	private Object isValue;
	
	/**
	 * 是否是and查询
	 */
	private boolean isAnd = true;
	
	/**
	 * 数据类型
	 */
	private DataType dataType;
	
	/**
	 * 用于自定义扩展
	 */
	private Object custom;
	
	/**
	 * 用于当前条件的后续条件，比如该实例还是用了and或者or方法，则会将该Condition对象放置到该链表中(这样就可以多级了)
	 */
	private List<Condition> conditionChain = new ArrayList<Condition>();
	
	/**
	 * 用于假如这个值有多个比较。ex:lt(1).gt(2)，这样就会都放入到这个里面。如果已经eq(1)，那么再进行lt这样的方法则会报错
	 */
	private LinkedHashMap<ConditionType, Object> conditions = new LinkedHashMap<ConditionType, Object>();
	
	/**
	 * 用于内部判断是否已经设置了eq的值
	 */
	private boolean hasSetIsValue = false;
	
	/**
	 * 其所属的QueryBuilder
	 */
	private QueryBuilder queryBuilder;
	
	/**
	 * 是否拥有or条件
	 */
	private boolean hasOrOption = false;
	
	public Condition(String field, DataType dataType){
		this.fieldName = field;
		this.dataType = dataType;
	}
	
	/**
	 * 
	 * 标题: intValue <br />
	 * 描述: int数据类型的查询条件
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Condition intValue(String fieldName){
		return new Condition(fieldName, DataType.INT);
	}
	
	/**
	 * 
	 * 标题: stringValue <br />
	 * 描述: string数据类型的查询条件
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Condition stringValue(String fieldName){
		return new Condition(fieldName, DataType.STRING);
	}
	
	/**
	 * 
	 * 标题: longValue <br />
	 * 描述: 长整型的查询条件
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Condition longValue(String fieldName){
		return new Condition(fieldName, DataType.LONG);
	}
	
	/**
	 * 
	 * 标题: floatValue <br />
	 * 描述: 单浮点型的查询条件 
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Condition floatValue(String fieldName){
		return new Condition(fieldName, DataType.FLOAT);
	}
	
	/**
	 * 
	 * 标题: doubleValue <br />
	 * 描述: 双浮点型的查询条件
	 * @param fileName
	 * @return 当前对象本身
	 */
	public static Condition doubleValue(String fileName){
		return new Condition(fileName, DataType.DOUBLE);
	}
	
	/**
	 * 
	 * 标题: booleanValue <br />
	 * 描述: 布尔型的查询条件 
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Condition booleanValue(String fieldName){
		return new Condition(fieldName, DataType.BOOLEAN);
	}
	
	/**
	 * 
	 * 标题: dateValue <br />
	 * 描述: 日期型的查询条件 
	 * @param fieldName
	 * @param 在smartSearch中用于数据验证
	 * @return 当前对象本身
	 */
	public static Condition dateValue(String fieldName, String pattern){
		return new Condition(fieldName, DataType.DATE).setCustom(pattern);
	}
	
	/**
	 * 
	 * 标题: dateValue <br />
	 * 描述: 日期型的查询条件 
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Condition dateValue(String fieldName){
		return dateValue(fieldName, null);
	}
	
	/**
	 * 
	 * 标题: eq <br />
	 * 描述: 相等。如果设置了该值则不可以进行lt或者gt之类值的确定，因为eq已经确定了。
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition eq(Object value){
		if(this.hasSetIsValue){	// 如果已经设置过值了
			throw new IllegalArgumentException("当前Condition已经为"+fieldName+"设置过eq的值，不可进行重复添加");
		}
		if(conditions.size() > 0){	// 如果当前有别的条件的话，则同样会抛出异常
			throw new IllegalArgumentException("当前Condition已经有了其他的判断条件，不可添加eq条件，这样是重复的");
		}
		this.isValue = value;
		this.hasSetIsValue = true;
		return this;
	}
	
	/**
	 * 
	 * 标题: neq <br />
	 * 描述: 不相等 
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition neq(Object value){
		conditions.put(ConditionType.NOT_EQUALS, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: lt <br />
	 * 描述: 小于
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition lt(Object value){
		conditions.put(ConditionType.LT, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: lte <br />
	 * 描述: 小于等于 
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition lte(Object value){
		conditions.put(ConditionType.LT_EQUALS, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: gt <br />
	 * 描述: 大于 
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition gt(Object value){
		conditions.put(ConditionType.GT, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: gte <br />
	 * 描述: 大于等于 
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition gte(Object value){
		conditions.put(ConditionType.GT_EQUALS, value);
		return this;
	}

	/**
	 * 
	 * 标题: in <br />
	 * 描述: 在...里
	 * @param values
	 * @return 当前对象本身
	 */
	public Condition in(Object... values){
		conditions.put(ConditionType.IN, Arrays.asList(values));	// 如果是in条件判断的话则会设置为列表形式
		return this;
	}

	/**
	 * 
	 * 标题: like <br />
	 * 描述: 像...，比如SQL中的like %x%
	 * @param isValue
	 * @return 当前对象本身
	 */
	public Condition like(Object value){
		conditions.put(ConditionType.LIKE, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: prifixMatch <br />
	 * 描述: 前缀匹配 
	 * @param value
	 * @return 当前对象本身
	 */
	public Condition prifixMatch(Object value){
		conditions.put(ConditionType.PRIFIX_MATCH, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: suffixMatch <br />
	 * 描述: 后缀匹配 
	 * @param value
	 * @return 当前对象本身
	 */
	public Condition suffixMatch(Object value){
		conditions.put(ConditionType.SUFFIX_MATCH, value);
		return this;
	}
	
	/**
	 * 
	 * 标题: and <br />
	 * 描述: and条件
	 * @param conditions
	 * @return 当前对象本身
	 */
	public Condition and(Condition condition){
		this.conditionChain.add(condition);
		return this;
	}

	/**
	 * 
	 * 标题: or <br />
	 * 描述: or条件
	 * @param conditions
	 * @return 当前对象本身
	 */
	public Condition or(Condition condition){
		if(this.queryBuilder != null)
			this.queryBuilder.setHasOrOption(true);
		this.hasOrOption = true;
		this.conditionChain.add(condition.setAnd(false));
		return this;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public Condition setFieldName(String fileName) {
		this.fieldName = fileName;
		return this;
	}

	public Object getIsValue() {
		return isValue;
	}

	public Condition setIsValue(Object value) {
		this.isValue = value;
		return this;
	}

	public boolean isAnd() {
		return isAnd;
	}

	public Condition setAnd(boolean isAnd) {
		this.isAnd = isAnd;
		return this;
	}

	public DataType getDataType() {
		return dataType;
	}

	public Condition setDataType(DataType dataType) {
		this.dataType = dataType;
		return this;
	}

	public Object getCustom() {
		return custom;
	}

	public Condition setCustom(Object custom) {
		this.custom = custom;
		return this;
	}

	public List<Condition> getConditionChain() {
		return conditionChain;
	}

	public Condition setConditionChain(List<Condition> conditionChain) {
		this.conditionChain = conditionChain;
		return this;
	}

	public LinkedHashMap<ConditionType, Object> getConditions() {
		return conditions;
	}

	public Condition setConditions(LinkedHashMap<ConditionType, Object> condition) {
		this.conditions = condition;
		return this;
	}

	public boolean getHasSetIsValue() {
		return hasSetIsValue;
	}

	public Condition setHasSetIsValue(boolean hasSetIsValue) {
		this.hasSetIsValue = hasSetIsValue;
		return this;
	}

	QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	Condition setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
		return this;
	}

	public boolean isHasOrOption() {
		return hasOrOption;
	}

}
