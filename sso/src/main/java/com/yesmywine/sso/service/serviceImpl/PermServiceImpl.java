package com.yesmywine.sso.service.serviceImpl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.dao.PermDao;
import com.yesmywine.sso.service.PermService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SJQ on 2017/6/9.
 */
@Service
public class PermServiceImpl extends BaseServiceImpl<Perms,Integer> implements PermService {
    @Autowired
    private PermDao permDao;
    @Override
    public Perms create(Perms perms) throws YesmywineException {
        checkPermKeyRepeat(perms.getPermKey());
        checkPermsNameRepeat(perms.getPermName());
        Integer parentId = perms.getParentId();
        if (parentId==null){
            perms.setParentId(0);
            perms.setLevel(1);
            perms.setCanDelete(true);
        }
        Perms parentPerms = permDao.findOne(parentId);
        ValueUtil.verifyNotExist(parentPerms,"父级不存在");

        permDao.save(perms);
        return perms;
    }

    @Override
    public Perms update(Perms perms) throws YesmywineException {
        Perms oldPerms = permDao.findOne(perms.getId());
        if(!oldPerms.getPermKey().equals(perms.getPermKey())){
            checkPermKeyRepeat(perms.getPermKey());
        }
        if(!oldPerms.getPermName().equals(perms.getPermName())){
            checkPermsNameRepeat(perms.getPermName());
        }
        ValueUtil.verifyNotExist(oldPerms,"无此权限！");
        Integer parentId = perms.getParentId();
        oldPerms.setPermKey(perms.getPermKey());
        oldPerms.setPermName(perms.getPermName());
        oldPerms.setIsMenu(perms.getIsMenu());
        if (parentId==null){
            oldPerms.setParentId(0);
            oldPerms.setLevel(1);
        }else{
            Perms parentPerms = permDao.findOne(parentId);
            ValueUtil.verifyNotExist(parentPerms,"父级不存在");
            Integer parentLevel = parentPerms.getLevel();
            oldPerms.setLevel(parentLevel+1);
            oldPerms.setLevel(parentId);
        }
        permDao.save(oldPerms);
        return oldPerms;
    }

    @Override
    public String delete(Integer id) throws YesmywineException {
        List<Integer> roleIdList = permDao.findByPermId(id);
        if(roleIdList.size()>0){
            ValueUtil.isError("该权限已关联角色，无法删除");
        }else{
            permDao.delete(id);
        }
        return "SUCCESS";
    }

    public void checkPermKeyRepeat(String permKey) throws YesmywineException {
        Perms perms_key = permDao.findByPermKey(permKey);
        ValueUtil.verifyExist(perms_key,"权限key已存在");
    }

    public void checkPermsNameRepeat(String permName) throws YesmywineException {
        Perms perms_name = permDao.findByPermName(permName);
        ValueUtil.verifyExist(perms_name,"权限名称已存在");
    }
}
