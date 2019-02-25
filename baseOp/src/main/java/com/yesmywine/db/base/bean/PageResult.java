package com.yesmywine.db.base.bean;

import java.util.List;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class PageResult {

    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPage;
    private Integer totalCount;
    private boolean hasPrevPage = true;
    private boolean hasNextPage = true;
    private List content;

    private String condition;


    public PageResult() {
        this.pageSize = DefaultData.defaultPageSize;
    }

    public PageResult(Integer currentPage) {
        this.currentPage = currentPage;
        this.pageSize = DefaultData.defaultPageSize;
    }

    public PageResult(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;

        int rem = this.totalCount % this.pageSize;
        this.totalPage = this.totalCount / this.pageSize;

        if (rem != 0) {
            this.totalPage++;
        }

        if (this.currentPage <= 1) {
            this.hasPrevPage = false;
        }
        if (this.currentPage >= this.totalPage) {
            this.hasNextPage = false;
        }


    }
}
