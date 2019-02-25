package com.yesmywine.cms.service;

import com.yesmywine.cms.entity.AppAdvertising;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by hz on 7/5/17.
 */
public interface AppAdverService {

    String save(AppAdvertising appAdvertising)throws YesmywineException;

    String delete(Integer id)throws YesmywineException;
}
