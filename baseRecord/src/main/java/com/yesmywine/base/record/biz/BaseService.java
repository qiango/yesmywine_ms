package com.yesmywine.base.record.biz;

import com.yesmywine.base.record.bean.PageModel;

import java.util.Collection;
import java.util.List;

/**
 * Created by SJQ on 2017/2/9.
 */
public interface BaseService<T, ID> {
    T findOne(ID id);

    PageModel findAll(PageModel pageModel);

    T save(T t);

    List findAll();

    List findAll(Collection ids);

    Collection<T> save(Collection<T> collection);

}
