package com.yesmywine.goods.dao;

import com.yesmywine.goods.entity.ImgFile;
import com.yesmywine.goods.entity.PicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface ImgFileDao extends JpaRepository<ImgFile, Integer> {

    ImgFile[] findByFileName(String fileName);

    ImgFile findById(Integer id);

}
