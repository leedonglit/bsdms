package com.isec.base.monit.service;

import cn.hutool.core.util.IdUtil;
import com.core.tools.AppUserTool;
import com.isec.base.monit.dao.AcaLogsDao;
import com.isec.base.monit.dto.AcaLogsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AcaLogsService {

    @Autowired
    AcaLogsDao acaLogsDao;

    public void saveAcaLogs(String title,String content,String acaId){
        AcaLogsDto acaLogsDto = new AcaLogsDto();
        acaLogsDto.setLOG_DESC(content);
        acaLogsDto.setLOG_TITLE(AppUserTool.getAppUser().getUserName()+" "+title);
        acaLogsDto.setLOG_TIME(new Date());
        acaLogsDto.setACA_ID(acaId);
        acaLogsDto.setLOG_ID(IdUtil.fastSimpleUUID());
        acaLogsDao.saveObject(acaLogsDto);
    }

    public List<AcaLogsDto> getLogsByAca(String acaId){

        return acaLogsDao.getList("SELECT * FROM CAS_BMDMS_LOG_TAB WHERE ACA_ID = ? ORDER BY LOG_TIME ASC",acaId);
    }
}
