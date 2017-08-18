package me.ezjs.core.model;

import me.ezjs.core.Constants;

import java.io.*;

/**
 * Created by zero-mac on 16/6/26.
 */
public class FlipFilter extends DAOFilter {

    public FlipFilter() {
    }

    public FlipFilter(Class clazz) {
        super(clazz);
    }

    private int pageNo;
    private int pageSize = Constants.DEFAULT_PAGE_SIZE;

    /**
     * 此次查询的总条数
     */
    private int totalCount = 0;

    private int totalPage = 1;

    private int pageStart = 1;
    private int pageEnd = 1;

    private int offset;//偏移量.

    private int offsetId;//TODO:逆向排序时,数据新增后,分页有问题

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void updatePageNo(Integer pageNo) {
        if (pageNo != null) {
            this.pageNo = pageNo.intValue();
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
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

    public void updateOffset() {
        setOffset((getPageNo() - 1) * getPageSize());
    }

    public void validate() {
        if (getPageNo() < 1) {
            // throw new DAOException("Invalid page no.");
            // change to default instead.
            setPageNo(1);
        }
        if (getPageSize() <= 0 || getPageSize() > 10000) {
            setPageSize(Constants.DEFAULT_PAGE_SIZE);
        }
        setOffset((getPageNo() - 1) * getPageSize());
        return;
    }

    @Override
    public FlipFilter deepClone() {
        try {
            // 将对象写到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo;
            oo = new ObjectOutputStream(bo);
            oo.writeObject(this);
            // 从流里读出来
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (FlipFilter) (oi.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
