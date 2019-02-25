package com.yesmywine.base.record.biz;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@NoRepositoryBean
public class BaseBiz<T, ID extends Serializable> {

    @Autowired
    private BaseRepository<T, ID> baseRepository;

    public List findAll() {
        List result = null;
        try {
            result = baseRepository.findAll();
            if (null != result && result.size() > 0) {
                Method after = result.get(0).getClass().getMethod("afterFind");
                for (int i = 0; i < result.size(); i++) {
                    after.invoke(result.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public List findAll(Collection ids) {
        return baseRepository.findAll(ids);
    }

    @Cacheable(value = "all", key = " #key")
    public PageModel findAll(PageModel pageModel, String key) {
        return findAll(pageModel);
    }

    //	@Cacheable(value = "User", key = "'hello'")
    public PageModel findAll(PageModel pageModel) {
        List result = null;
        try {
//			if (null != page && conditions.size() > 0 ){
//				return 1;//有分页有条件
//			} else if ( null != page && conditions.size() == 0 ) {
//				return 2;//有分页无条件
//			} else if (null == page && conditions.size() >0 ){
//				return 3;//无分页有条件
//			} else {
//				return 4;//无分页无条件
//			}
            switch (pageModel.getKind()) {
                case 1:
                    Page<T> page1 = baseRepository.findAll(pageModel.getWhereClause(), pageModel.getPageable());
                    result = page1.getContent();
                    pageModel.setTotalPages(page1.getTotalPages());
                    pageModel.setTotalRows(page1.getTotalElements());
                    break;
                case 2:
                    Page<T> page2 = baseRepository.findAll(pageModel.getWhereClause(),pageModel.getPageable());
                    result = page2.getContent();
                    pageModel.setTotalPages(page2.getTotalPages());
                    pageModel.setTotalRows(page2.getTotalElements());
                    break;
                case 3:
                    result = baseRepository.findAll(pageModel.getWhereClause());
                    break;
                case 4:
                    result = baseRepository.findAll();
                    break;
            }
            if (null != result && result.size() > 0) {
                Method after = result.get(0).getClass().getMethod("afterFind", String.class);
                for (int i = 0; i < result.size(); i++) {
                    after.invoke(result.get(i), pageModel.getFields());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pageModel.setContent(result);
        return pageModel;
    }


    public T findOne(ID id) {
        T result = null;
        try {
            result = baseRepository.findOne(id);
            if (null != result) {
                Method after = result.getClass().getMethod("afterFind");
                after.invoke(result);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public T save(T entity) {
        T result = null;
        try {
            Method before = entity.getClass().getMethod("beforeSave");
            before.invoke(entity);
            result = baseRepository.save(entity);
            Method after = entity.getClass().getMethod("afterSave");
            after.invoke(entity);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public Collection<T> save(Collection<T> entities) {
        Collection<T> result = null;
//		try {
        baseRepository.save(entities);
//			Method before = entities.getClass().getMethod("beforeSave");
//			before.invoke(entity);
        result = baseRepository.save(entities);
//			Method after = entity.getClass().getMethod("afterSave");
//			after.invoke(entity);
        return result;
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.getTargetException().printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} finally {
//			return null;
//		}
    }


}
