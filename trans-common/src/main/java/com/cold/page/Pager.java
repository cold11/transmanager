package com.cold.page;


import com.cold.util.Global;

import java.util.List;


public class Pager<T> {

	/**每页显示*/
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小.
	/**页码*/
	private int pageNo = 1;
	private int totalPages;//总页数
	/**开始数*/
	private int start = 0;
	
	private List<T> result;
	/**总条数*/
	private long totalRows = 0;
	
	private Object condition;

	public Pager(){
		
	}
	
	public Pager(int pageSize, int pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }
	
    /**
     * @param pageSize
     *          每页条数
     * @param pageNo
     *          页码（单前页码）
     * @param totalRows
     *          总条数
     * @param start
     *          开始条数
     * @param result
     *          返回数据list
     */
	public Pager(int pageSize, int pageNo, long totalRows, int start, List<T> result){
		
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.totalRows = totalRows;
		this.start = start;
		this.result = result;
	}
	
    /**
     * <p>
     * 每一页的条数，默认10条
     * <p/>
     */
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getStart() {
		this.start = (pageNo - 1) * pageSize;
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}


	public List<?> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public long getPageCount(){
		return getTotalRows() % getPageSize() == 0 ? getTotalRows() / getPageSize() : getTotalRows() / getPageSize() + 1;
	}


	/**
	 * 是否进行总数统计
	 * @return this.totalRows==-1
	 */
	public boolean isNotCount() {
		return this.totalRows==-1;
	}

	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getTotalRows()) {
			firstResult = 0;
		}
		return firstResult;
	}

	public int getLastResult(){
		int lastResult = getFirstResult()+getMaxResults();
		if(lastResult>getTotalRows()) {
			lastResult =(int) getTotalRows();
		}
		return lastResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}


	public Object getCondition() {
		return condition;
	}

	public void setCondition(Object condition) {
		this.condition = condition;
	}

	public int getTotalPages() {
		if(totalRows>0){
			totalPages  = (int)Math.ceil((double)totalRows/pageSize);
		}
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
