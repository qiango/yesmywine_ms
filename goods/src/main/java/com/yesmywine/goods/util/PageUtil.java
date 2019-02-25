package com.yesmywine.goods.util;


import org.springframework.data.domain.Page;

/**
 * Created by taylor on 9/2/16.
 * twitter: @taylorwang789
 */
public class PageUtil {

    public static PageResult toPage(Page<?> page, Integer pageNumber){
        PageResult pageResult = new PageResult();
        pageResult.setContent(page.getContent());
        pageResult.setTotalPage(page.getTotalPages());
        pageResult.setPage(pageNumber);
        pageResult.setSize(page.getSize());
        pageResult.setHasNext(page.hasNext());
        pageResult.setHasPrevious(page.hasPrevious());
        return pageResult;
    }

}
