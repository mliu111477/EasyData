package com.easydata.core.query;

/**
 * 排序对象。<br />
 *
 * @author Mr.Pro
 */
public class Sort {

	private String fieldName;	// 域
	private boolean isAsc;		// true:asc, false:desc
	
	public Sort(String fieldName, boolean isAsc) {
		super();
		this.fieldName = fieldName;
		this.isAsc = isAsc;
	}

	/**
	 * 
	 * 标题: asc <br />
	 * 描述: 从小到大
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Sort asc(String fieldName){
		return new Sort(fieldName, true);
	}
	
	/**
	 * 
	 * 标题: desc <br />
	 * 描述: 从大到小 
	 * @param fieldName
	 * @return 当前对象本身
	 */
	public static Sort desc(String fieldName){
		return new Sort(fieldName, false);
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public boolean isAsc() {
		return isAsc;
	}
	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
	
}
