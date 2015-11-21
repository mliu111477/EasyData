package com.easydata.core.query;

import java.util.List;

/**
 * 搜索结果。<br />
 *
 * @author Mr.Pro
 */
public class QueryResult<T> {

	// 查询状态
	private int status;
	
	// 指定查询结果(分页中的数据)
	private List<T> items;
	
	// 该查询的总共数量
	private long totalCount;

	public int getCode() {
		return status;
	}

	public void setCode(int code) {
		this.status = code;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
}
