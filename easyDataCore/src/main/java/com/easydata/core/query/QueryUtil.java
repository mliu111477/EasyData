package com.easydata.core.query;


import com.easydata.core.query.datatype.IDataTypeAcceptor;
import com.easydata.core.utils.StringUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 用于提供query一些工具方法。<br />
 *
 * @author Mr.Pro
 */
public class QueryUtil {

	/**
	 * 
	 * 标题: smartCondition <br />
	 * 描述: 对这个condition进行智能过滤，过滤掉那些不需要的查询条件 
	 * @param condition 查询条件
	 * @return 过滤掉之后的查询条件，如果为null则代表当前的查询条件中并不存在可以使用的条件
	 * @throws IllegalArgumentException 参数错误
	 */
	public static Condition smartCondition(Condition condition) throws IllegalArgumentException{
		QueryBuilder queryBuilder = condition.getQueryBuilder();
		if(queryBuilder != null && queryBuilder.getSmartAcceptor() != null){	// 如果这个Query指定了数据校验器
			return smartCondition(condition, queryBuilder.getSmartAcceptor());
		}else{
			return smartCondition(condition, QueryContract.DEFAULT_DATA_ACCEPTOR);
		}
	}
	
	/**
	 * 
	 * 标题: smartCondition <br />
	 * 描述: 对condition进行只能过滤，过滤掉那些不需要的查询条件 
	 * @param condition 查询条件
	 * @param acceptor 具体的验证器
	 * @return 过滤掉之后的查询条件，如果为null则代表当前的查询条件中并不存在可以使用的条件
	 * @throws IllegalArgumentException 参数错误
	 */
	public static Condition smartCondition(Condition condition, IDataTypeAcceptor acceptor) throws IllegalArgumentException{
		validCondition(condition);	// 先进行验证

		boolean resIsUseCurrentValue = true;	// 最后的结果是否使用当前的结果
		if(!condition.getHasSetIsValue()){	// 如果当前并不是eq的条件(使用了like或者lt之类的方法)
			LinkedHashMap<ConditionType, Object> map = condition.getConditions();
			for(Iterator<ConditionType> its = map.keySet().iterator();its.hasNext();){
				ConditionType type = its.next();
				Object val = acceptor.accept(condition.getDataType(), map.get(type), condition);
				if(val == null){	// 如果过滤没通过
					map.remove(type);
				}else{
					map.put(type, val);	// 重新设置目标值	
				}
			}
			if(map.isEmpty()){	// 如果已经没有条件了(都已经在上面的for循环被过滤掉了)
				resIsUseCurrentValue = false;
			}
		} else { // 否则只是用了eq方法
			Object val = acceptor.accept(condition.getDataType(), condition.getIsValue(), condition);
			if(val != null){	// 如果验证通过
				condition.setIsValue(val);
			}else{	// 如果这里没有验证通过则不使用当前的对象
				resIsUseCurrentValue = false;
			}
		}
		
		if(resIsUseCurrentValue){	// 如果使用当前的对象
			checkConditionChain(condition);
		}else{
			// 否则创建新的查询，判断是否有链
			if(condition.getConditionChain() != null && condition.getConditionChain().size() > 0){
				Condition newCondition = condition.getConditionChain().remove(0);	// 获取其中的第一个元素作为当前要返回的对象
				newCondition.getConditionChain().addAll(condition.getConditionChain());	// 增加原先的condition链表中的元素到当前的新的condition后面
				condition = newCondition;
				checkConditionChain(condition);
			}else{
				condition = null;	// 否则则直接返回null，因为当前的condition并不存在查询条件并且没有chain
			}
		}
		
		// 最终进行判断该对象是否拥有属性
		if(condition != null && condition.getConditions().isEmpty() && condition.getIsValue() == null){
			condition = null;
		}
		
		// 如果该对象并没有
		return condition;
	}
	
	/**
	 * 
	 * 标题: checkConditionChain <br />
	 * 描述: 用于内部进行condition中的链表检查 
	 * @param condition condition
	 */
	private static void checkConditionChain(Condition condition){
		if(condition.getConditionChain() != null && condition.getConditionChain().size() > 0){
			// 遍历其链接的所有信息
			for(Iterator<Condition> chainIt = condition.getConditionChain().iterator(); chainIt.hasNext();){
				Condition chainCondition = chainIt.next();
				Condition afterSmart = smartCondition(chainCondition);	// 对其进行递归过滤
				if(afterSmart != null){	// 如果验证通过，则拷贝全部的新属性到当前chain上的对象
					chainCondition.setFieldName(afterSmart.getFieldName());
					chainCondition.setIsValue(afterSmart.getIsValue());
					chainCondition.setAnd(afterSmart.isAnd());
					chainCondition.setDataType(afterSmart.getDataType());
					chainCondition.setCustom(afterSmart.getCustom());
					chainCondition.setConditionChain(afterSmart.getConditionChain());
					chainCondition.setConditions(afterSmart.getConditions());
					chainCondition.setHasSetIsValue(afterSmart.getHasSetIsValue());
				}else{	// 验证不通过的话则移除掉当前的condition
					chainIt.remove();
				}
			}
		}
	}
	
	/**
	 * 
	 * 标题: validCondition <br />
	 * 描述: 当使用到当前的condition的时候，可以先使用该方法验证该condition是否有效。这里并不会对其condition的链表进行检索有效性。 
	 * @param condition 当期的条件
	 * @throws IllegalArgumentException 如果当前的条件中存在问题，则会通过抛出异常的形式进行提示
	 */
	public static void validCondition(Condition condition) throws IllegalArgumentException{
		if(condition == null)
			throw new IllegalArgumentException("condition不可为null值");
		
		if(StringUtil.isEmpty(condition.getFieldName()))
			throw new IllegalArgumentException("condition的查询属性名称不能为空");
		
		if(!condition.getHasSetIsValue() && condition.getConditions().size() == 0){
			throw new IllegalArgumentException("请至少增加一个查询条件");
		}
	}
}
