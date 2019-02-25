package com.yesmywine.evaluation.service;

import com.yesmywine.evaluation.bean.Lable;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by hz on 6/21/17.
 */

public interface LableService {
    String save(Lable lable) throws YesmywineException;
    String delete(Integer id) throws YesmywineException;
}
