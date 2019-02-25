package com.yesmywine.goods.dao;

import com.yesmywine.goods.entity.PicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface PicFileDao extends JpaRepository<PicFile, Integer> {

    PicFile[] findByTempFileName(String tempFileName);

    PicFile[] findByFormalFileName(String formalFileName);
}
