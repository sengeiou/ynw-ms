package com.ynw.system.util;

import java.util.List;

/**
 * 分页工具类
 * @param <T> 换型
 */
public class PageUtil<T> {
	private Integer nowPage;//当前页
	private Integer pageSize;//每页数量
	private Integer count;//数据总数
	private Integer totalPage;//总页数
	private Integer prevPage;//上一页
	private Integer nextPage;//下一页
	private Integer firstPage;//第一页
	private Integer lastPage;//最后一页
	private List<T> list;//分页集合
	
	public PageUtil() {
		super();
	}
	public PageUtil(Integer nowPage, Integer pageSize, Integer count,
                    List<T> list) {
		super();
		this.nowPage = nowPage;
		this.pageSize = pageSize;
		this.count = count;
		this.list = list;
	}
	public Integer getNowPage() {
		return nowPage;
	}
	public void setNowPage(Integer nowPage) {
		this.nowPage = nowPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Integer getTotalPage() {
		return (int)Math.ceil(count*1.0/pageSize);
	}
	public Integer getPrevPage() {
		return (nowPage-1)<1?1:(nowPage-1);
	}
	public Integer getNextPage() {
		return (nowPage+1)>getTotalPage()?getTotalPage():(nowPage+1);
	}
	public Integer getFirstPage() {
		return 1;
	}
	public Integer getLastPage() {
		return getTotalPage();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
