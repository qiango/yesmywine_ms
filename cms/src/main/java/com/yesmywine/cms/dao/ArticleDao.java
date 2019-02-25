package com.yesmywine.cms.dao;


import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.ArticleEntity;
import com.yesmywine.cms.entity.ColumnsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by liqingqing on 2017/1/4.
 */
@Repository
public interface ArticleDao extends BaseRepository<ArticleEntity, Integer> {
    ArticleEntity findById(Integer id);


    @Query(value = "SELECT id,title,abstracts,createTime FROM article WHERE columnsName=:columnName ORDER BY createTime DESC LIMIT :num",nativeQuery =true)
    List findByColumnsName(@Param("columnName")Integer columnName,@Param("num") Integer num);

    @Query(value = "select id,title from article WHERE columnsName =:columnName",nativeQuery = true)
//    @Query("select id ,title from ArticleEntity where columnsEntity =:columnName")
    List findIdAndTitle(@Param("columnName")Integer columnName);
    @Query("select count(*) from ArticleEntity")
    Integer findCount();

    List<ArticleEntity> findByColumnsEntity(ColumnsEntity columnsEntity);
}
