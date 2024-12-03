package com.isec.base.monit.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.common.EncryptUtil;
import com.core.tools.AppUserTool;
import com.isec.base.monit.dao.GroupDao;
import com.isec.base.monit.dto.GroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    GroupDao groupDao;

    @Autowired
    AcaLogsService acaLogsService;

    public List<GroupDto> getGroupsByAca(String acaId){
        return groupDao.getGroupsByAca(acaId);
    }

    public GroupDto getGroupByAca(String userId,String acaId){
        return groupDao.getObject("SELECT * FROM CAS_BMDMS_GROUP_TAB WHERE GP_USER = ? AND ACA_ID = ? ORDER BY GP_ADD_TIME DESC LIMIT 1",userId,acaId);
    }

    public boolean removeGroup(String group){
        groupDao.excute("DELETE FROM CAS_BMDMS_GROUP_TAB WHERE GP_ID = ?",group);
        return true;
    }

    public boolean beManager(String group){
        GroupDto groupDto = groupDao.getObject(group);
        if (groupDto.getGP_ROLE() == 0){//普通用户
            groupDao.excute("UPDATE CAS_BMDMS_GROUP_TAB SET GP_ROLE = 1 WHERE GP_ID = ?",group);
        }
        if (groupDto.getGP_ROLE() == 1){//管理员
            groupDao.excute("UPDATE CAS_BMDMS_GROUP_TAB SET GP_ROLE = 0 WHERE GP_ID = ?",group);
        }
        return true;
    }

    @Transactional
    public boolean addGroup(String param){
        try{
            param = Base64.decodeStr(param,"UTF-8");
            JSONObject json = JSONUtil.parseObj(param);
            GroupDto groupDto = getGroupByAca(AppUserTool.getAppUser().getUserID(),json.getStr("ACA_ID"));
            if (null == groupDto && new Date().getTime()/1000 < json.getInt("failureTime")){
                groupDto = new GroupDto();
                groupDto.setGP_ROLE(0);
                groupDto.setGP_ADD_TIME(new Date());
                groupDto.setGP_USER(AppUserTool.getAppUser().getUserID());
                groupDto.setGP_ID(IdUtil.fastSimpleUUID());
                groupDto.setACA_ID(json.getStr("ACA_ID"));
                groupDao.saveObject(groupDto);
                acaLogsService.saveAcaLogs("Join a teammate",String.format("<p>%s Joined the project team through the address shared by %s</p>",AppUserTool.getAppUser().getUserName(),json.getStr("shareUser")),groupDto.getACA_ID());
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
