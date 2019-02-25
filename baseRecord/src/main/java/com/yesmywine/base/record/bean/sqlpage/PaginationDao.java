package com.yesmywine.base.record.bean.sqlpage;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by by on 2017/6/28.
 */
public abstract class PaginationDao {
    protected SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 通过Finder获得分页数据
     *
     * @param finder
     *            Finder对象
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页条数
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Pagination find(Finder finder, int pageNo, int pageSize) {
        int totalCount = countQueryResult(finder);
        Pagination p = new Pagination(pageNo, pageSize, totalCount);
        if (totalCount < 1) {
            p.setList(new ArrayList());
            return p;
        }
        Query query = getSession().createQuery(finder.getOrigHql());
        finder.setParamsToQuery(query);
        query.setFirstResult(p.getFirstResult());
        query.setMaxResults(p.getPageSize());
        if (finder.isCacheable()) {
            query.setCacheable(true);
        }
        List list = query.list();
        p.setList(list);
        return p;
    }

    /**
     * 获得Finder的记录总数
     *
     * @param finder
     * @return
     */
    protected int countQueryResult(Finder finder) {
        Query query = getSession().createQuery(finder.getRowCountHql());
        finder.setParamsToQuery(query);
        if (finder.isCacheable()) {
            query.setCacheable(true);
        }
        return ((Number) query.iterate().next()).intValue();
    }
}
