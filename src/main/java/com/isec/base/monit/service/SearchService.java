package com.isec.base.monit.service;

import com.core.security.page.JsonResult;
import com.core.tools.AppUserTool;
import com.isec.base.monit.dto.AcademicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    MessageService messageService;


    @Autowired
    AcademicService academicService;

    public JsonResult applyKey(String acaId){
        if (AppUserTool.isLogin()){
            AcademicDto academicDto = academicService.getAcaById(acaId);
            messageService.saveMessage("Apply for key",String.format("%s applies to view your project 【 %s 】, please assign a KEY", AppUserTool.getAppUser().getUserName(),academicDto.getACA_TITLE()),academicDto.getACA_CREATOR(),acaId);
            return new JsonResult("Apply Success!",true);
        }
        return new JsonResult("Apply Failed! Please Login",false);
    }

}
