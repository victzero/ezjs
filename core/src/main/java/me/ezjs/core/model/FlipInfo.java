package me.ezjs.core.model;

import java.io.Serializable;

/**
 * Created by zero-mac on 16/7/10.
 */
public class FlipInfo implements Serializable{

    public FlipInfo(FlipFilter filter, long totalCount) {
        this.pageNo = filter.getPageNo();
        this.pageSize = filter.getPageSize();
        this.totalCount = totalCount;
        this.totalPage = filter.getTotalPage();
        this.pageStart = filter.getPageStart();
        this.pageEnd = filter.getPageEnd();
        this.offset = filter.getOffset();
        this.init();
    }


    private int pageNo;
    private int pageSize;

    /**
     * 此次查询的总条数
     */
    private long totalCount = 0;

    private int totalPage = 1;

    private int pageStart = 1;
    private int pageEnd = 1;

    private int offset;//偏移量.

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageStart() {
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageEnd() {
        return pageEnd;
    }

    public void setPageEnd(int pageEnd) {
        this.pageEnd = pageEnd;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    /**
     * 分页参数初始化.在调用该方法前,必须设定的参数如下:
     * <p/>
     * pageNo,totalCount,pageSize
     */
    public void init() {
        totalPage = (int) (totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            totalPage++;
        }

        this.pageStart = pageNo - 3 >= 1 ? pageNo - 3 : 1;

        pageEnd = pageNo + 3 <= totalPage ? pageNo + 3 : totalPage;
        if (pageNo > pageEnd) {
            pageNo = pageEnd;
        }
    }
}
