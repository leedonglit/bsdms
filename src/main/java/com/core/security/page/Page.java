package com.core.security.page;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T> Page中记录的类型.
 * @author LiuTao
 */
public class Page<T> {
	/**
	 * 排序字段 如果多个排序以 “,” 分割
	 */
    private String                orderby;                                        //排序
    private String                sort;
    private String                key;
    /**
     * freemarker 字符串
     */
    private String				  html;
    /**
     * 排序方式  默认是 ASC
     */
    private String                dir             = "asc";
    /**
     * 本Page对象应用场景的默认每页条数
     */
    protected int                 defaultPageSize = 30;
    //分页参数 //
    protected int                 pageNo          = 1;
    //默认分页条数
    protected int                 pageSize        = defaultPageSize;
    protected boolean             autoCount       = true;
    protected Integer[]	 pageSizes = new Integer[]{10,20,30,50,100};
    //返回结果 //
    protected List<T>             result;
    protected long                totalCount      = -1;
    protected long                totalpage      = -1;
    protected Map<String, Object> params             = new HashMap<String, Object>();
    // 构造函数 //
    public Page() {}

    public Page(final int pageSize) {
        setPageSize(pageSize);
    }
    public Page(final int pageSize, final int pageNo) {
        setPageSize(pageSize);
        setPageNo(pageNo);
    }
    public Page(final int pageSize, final boolean autoCount) {
        setPageSize(pageSize);
        setAutoCount(autoCount);
    }
    // 查询参数访问函数 //
    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNo() {
        return pageNo;
    }
    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public void setPageNo(final int pageNo) {
        if (pageNo > 0) {
            this.pageNo = pageNo;
        }
    }
    /**
     * 获得每页的记录数量,默认为1.
     */
    public int getPageSize() {
        return pageSize;
    }
    /**
     * 设置每页的记录数量,低于1时自动调整为1.
     */
    public void setPageSize(final int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }
    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return ((pageNo - 1) * pageSize) + 1;
    }
    /**
     * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
     */
    public boolean isAutoCount() {
        return autoCount;
    }
    /**
     * 查询对象时是否自动另外执行count查询获取总记录数.
     */
    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }
    // 访问查询结果函数 //
    /**
     * 取得页内的记录列表.
     */
    public List<T> getResult() {
        if (result == null) {
            result = new ArrayList<T>();
        }
        return result;
    }
    /**
     * 设置页内的记录列表.
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }
    /**
     * 取得总记录数, 默认值为-1.
     */
    public long getTotalCount() {
        return totalCount;
    }
    /**
     * 设置总记录数.
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
        selfFix();
    }
    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public long getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }
        totalpage = (totalCount - 1) / pageSize + 1;
        return totalpage;
        //              long count = totalCount / pageSize;
        //              if (totalCount % pageSize > 0) {
        //                      count++;
        //              }
        //              return count;
    }
    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }
    /**
     * 取得下页的页号, 序号从1开始.
     * 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext())
            return pageNo + 1;
        else
            return pageNo;
    }
    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageNo > 1);
    }
    /**
     * 取得上页的页号, 序号从1开始.
     * 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }
    public int getDefaultPageSize() {
        return defaultPageSize;
    }
    public void setDefaultPageSize(int defaultPageSize) {
        if (defaultPageSize > 0) {
            this.defaultPageSize = defaultPageSize;
        }
    }
    public int getMaxPageNo() {
        if (totalCount > 0) {
            return (int) ((totalCount - 1) / pageSize) + 1;
        }
        return 1;
    }
    /**
     * 如果设置的pageNo大于最大页码，则自动调整!
     */
    public void selfFix() {
        if (totalCount >= 0) {
            int maxPageNo = getMaxPageNo();
            if (pageNo > maxPageNo) {
                pageNo = maxPageNo;
            }
        }
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getDir() {
        return dir;
    }
    public void setDir(String dir) {
        this.dir = dir;
    }
    public boolean isUsingDefaultPageSize() {
        return defaultPageSize == pageSize;
    }
    public int getPageStart() {
        return getPageSize() * (getPageNo() - 1);
    }
    public String getOrderby() {
        return orderby;
    }
    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public long getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(long totalpage) {
		this.totalpage = totalpage;
	}

	public Integer[] getPageSizes() {
		return pageSizes;
	}

	public void setPageSizes(Integer[] pageSizes) {
		this.pageSizes = pageSizes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}