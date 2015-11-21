package com.easydata.core.query;

import com.easydata.core.query.datatype.IDataTypeAcceptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 搜索条件构建器。<br />
 *
 * @author Mr.Pro
 */
public class QueryBuilder {

	/**
	 * 是否开启智能搜索
	 */
	private boolean smartSearch = false;
	private List<Condition> conditions = new ArrayList<Condition>();
	private List<Sort> sorts = new ArrayList<Sort>();

	// 主要用于各个不同的平台增加自定义参数适用
	private Object customObject;
	
	// 增加分页相关属性
	private int start = 0;	// 起始值，从0开始
	private int size = 10;	// 显示多少条数据，默认是10条数据
	
	// 用于指定这个QueryBuilder中如果使用了smartSearch的话则可以自行扩展
	private IDataTypeAcceptor smartAcceptor;
	
	// 用于获取是否含有or的查询条件
	private boolean hasOrOption = false;

	public QueryBuilder(Condition condition) {
		if(condition.isHasOrOption())
			this.hasOrOption = true;
		this.conditions.add(condition.setQueryBuilder(this));
	}

	public static QueryBuilder where(Condition condition) {
		return new QueryBuilder(condition);
	}

	/**
	 * 
	 * 标题: and <br />
	 * 描述: 增加一个新的搜索条件
	 * 
	 * @param condition
	 *            增加一个新的条件，用and和之前的链接
	 * @return
	 */
	public QueryBuilder addCondition(Condition condition) {
		condition.setQueryBuilder(this);	// 设置引用
		return addToConditions(condition);
	}

	/**
	 * 
	 * 标题: sortBy <br />
	 * 描述: 增加排序
	 * 
	 * @param sort
	 * @return
	 */
	public QueryBuilder sortBy(Sort sort) {
		sorts.add(sort);
		return this;
	}

	/**
	 * 
	 * 标题: addToConditions <br />
	 * 描述: 增加条件的条件列表
	 * 
	 * @param condition
	 *            查询条件
	 * @return
	 */
	private QueryBuilder addToConditions(Condition condition) {
		conditions.add(condition);
		if(condition.isHasOrOption())
			this.hasOrOption = true;
		return this;
	}

	public boolean isSmartSearch() {
		return smartSearch;
	}

	/**
	 * 
	 * 标题: setSmartSearch <br />
	 * 描述: 设置是否启用只能搜索
	 * 
	 * @param smartSearch
	 */
	public QueryBuilder setSmartSearch(boolean smartSearch) {
		this.smartSearch = smartSearch;
		return this;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public static void main(String[] args) {
		// 测试构建 where id>=2 and name like '%z%' or (age > 1 and name <> '张三') and date > new Date()
		
		QueryBuilder query = QueryBuilder.where(Condition.intValue("id").gte(2)) // id >= 2
				// and name like '%z%' or (age > 18 or name like 
				.addCondition(Condition.stringValue("name").like("z").or(Condition.intValue("age").gt(1).and(Condition.stringValue("name").neq("张三"))))
				// and date > new Date()
				.addCondition(Condition.dateValue("date", "yyyy-MM-dd").lt(new Date()))
				.setSmartSearch(true); // 试一试使用smart和不适用smart的区别。(true:)

		// 在进行后台操作的时候
		List<Condition> conditions = query.getConditions();
		if (query.isSmartSearch()) { // 智能搜索就代表会自动过滤掉那些没有用的，可以在上面的搜索条件中给值设置为null进行测试
			for (Condition condition : conditions) {
				condition = QueryUtil.smartCondition(condition);
				printCondition(condition);
			}
		} else { // 非智能搜索则会列出所有的选择条件
			for (Condition condition : conditions) {
				printCondition(condition);
			}
		}
		
		// 测试：where a=1 and b=2 or c=2
//		Query.where(Condition.intValue("a").eq(1).and(null)).addCondition(condition);
	}

	/**
	 * 
	 * 标题: printCondition <br />
	 * 描述: 进行打印。处理过程：
	 * <ul>
	 * <li>1、首先判断是单独的值(isValue != null)还是多个的条件(isValue == null &&
	 * conditions.size() > 0)。</li>
	 * <li>2、遍历其链表。</li>
	 * </ul>
	 * 
	 * @param condition
	 */
	public static void printCondition(Condition condition) {
		if (condition == null)
			return;

		System.out.print((condition.isAnd() ? " and " : " or ") + "(");

		System.out.print(condition.getFieldName() + " ");
		if (condition.getHasSetIsValue()) { // 如果是相等条件的话
			System.out.print("= " + condition.getIsValue());
		} else { // 如果不是相等条件的话则可能会是多个条件
			for (Map.Entry<ConditionType, Object> entry : condition.getConditions().entrySet()) {
				System.out.print(entry.getKey() + " " + entry.getValue());
			}
		}

		// 如果当前存在链表的话，意思就是如果当前的Condition后面还有别的条件(使用了and或者or)
		if (condition.getConditionChain().size() > 0) {
			for (Condition con : condition.getConditionChain()) {
				printCondition(con);
			}
		}
		System.out.print(")");
	}

	public List<Sort> getSorts() {
		return sorts;
	}

	public Object getCustomObject() {
		return customObject;
	}

	public void setCustomObject(Object customObject) {
		this.customObject = customObject;
	}

	public int getStart() {
		return start;
	}

	/**
	 * 
	 * 标题: setStart <br />
	 * 描述: 设置分页起始条数，从0开始
	 * @param start
	 * @return
	 */
	public QueryBuilder setStart(int start) {
		this.start = start;
		return this;
	}

	public int getSize() {
		return size;
	}

	/**
	 * 
	 * 标题: setSize <br />
	 * 描述: 设置分页显示数量 
	 * @param size
	 * @return
	 */
	public QueryBuilder setSize(int size) {
		this.size = size;
		return this;
	}

	/**
	 * 
	 * 标题: getSmartAcceptor <br />
	 * 描述: 获取智能校验器 
	 * @return
	 */
	public IDataTypeAcceptor getSmartAcceptor() {
		return smartAcceptor;
	}

	/**
	 * 
	 * 标题: setSmartAcceptor <br />
	 * 描述: 设置智能的数据校验器 
	 * @param smartAcceptor
	 */
	public void setSmartAcceptor(IDataTypeAcceptor smartAcceptor) {
		this.smartAcceptor = smartAcceptor;
	}

	/**
	 * 
	 * 标题: isHasOrOption <br />
	 * 描述: 获取是否含有or的查询条件 
	 * @return
	 */
	public boolean isHasOrOption() {
		return hasOrOption;
	}

	void setHasOrOption(boolean hasOrOption) {
		this.hasOrOption = hasOrOption;
	}
	
}
