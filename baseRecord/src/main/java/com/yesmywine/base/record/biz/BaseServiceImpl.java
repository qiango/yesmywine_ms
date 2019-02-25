package com.yesmywine.base.record.biz;

import com.yesmywine.base.record.bean.PageModel;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by SJQ on 2017/2/10.
 */
@NoRepositoryBean
public class BaseServiceImpl<T, ID extends Serializable> extends BaseBiz<T, ID> implements BaseService<T, ID> {
    @Override
    public T findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public PageModel findAll(PageModel pageModel) {
        return super.findAll(pageModel);
    }

    @Override
    public T save(T t) {
        return super.save(t);
    }

    @Override
    public List findAll() {
        return super.findAll();
    }

    @Override
    public List findAll(Collection ids) {
        return super.findAll(ids);
    }

    @Override
    public Collection<T> save(Collection<T> collection) {
        return super.save(collection);
    }

}
