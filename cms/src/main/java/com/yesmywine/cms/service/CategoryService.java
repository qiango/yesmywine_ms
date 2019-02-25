
package com.yesmywine.cms.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.Category;


/**
 * Created by hz on 2/10/17.
 */

public interface CategoryService extends BaseService<Category, Integer> {

    Boolean synchronous(Integer id, String name, Integer parentId, Integer synchronous);
}

