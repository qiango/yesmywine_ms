//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yesmywine.goods.util;

import com.google.gson.annotations.Expose;
import java.util.List;

public class PageResult {
    @Expose
    private Integer totalPage;
    @Expose
    private Integer page;
    @Expose
    private Integer size;
    @Expose
    private List content;
    @Expose
    private boolean hasNext;
    @Expose
    private boolean hasPrevious;

    public PageResult() {
    }

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List getContent() {
        return this.content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean hasPrevious() {
        return this.hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
