package com.isec.base.monit.web;

import com.common.DateUtil;
import com.core.security.page.AutoPage;
import com.core.security.page.JsonResult;
import com.core.security.page.PageSupport;
import com.core.tools.AppUserTool;
import com.isec.base.monit.dto.AcademicDto;
import com.isec.base.monit.dto.DocumentDto;
import com.isec.base.monit.dto.UserDto;
import com.isec.base.monit.service.*;
import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @RequestMapping("/index")
    public ModelAndView uptoGradePage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("index1");
        return model;
    }

    @Autowired
    AcademicService academicService;

    @RequestMapping("/loadCheckData")
    @ResponseBody
    public JsonResult loadCheckData(){
        List<Map<String, Object>> classs = academicService.getLimitAcademicClass();
        List<Map<String, Object>> authors = academicService.getLimitAcademicAuthor();
        List<Map<String, Object>> orgs = academicService.getLimitAcademicOrg();
        List<Map<String, Object>> years = academicService.getLimitAcademicYears();
        Map<String,Object> map = new HashMap<>();
        map.put("classs",classs);
        map.put("authors",authors);
        map.put("orgs",orgs);
        map.put("years",years);
        String s = PageSupport.loadObjHtml("/module/base/search/include/checkList", map);
        return new JsonResult(s);
    }

    @Autowired
    DocumentService documentService;

    @Autowired
    UserService userService;

    @RequestMapping("/searchAca/{year}")
    @ResponseBody
    public JsonResult searchAca(@RequestParam String jsonpage,@PathVariable("year") String year){
        AutoPage<AcademicDto> page = PageSupport.fromJsonAutoPage(jsonpage);
        page = academicService.searchAca(page);
        List<AcademicDto> acas = page.getResult();
        //根据年份过滤
        if(!"1970".equals(year)){
            acas = acas.stream().filter((aca)->{
                return DateUtil.getYear(aca.getACA_CREATE_TIME())== Integer.parseInt(year);
            }).collect(Collectors.toList());
        }
        List<DocumentDto> docs = new ArrayList<>();
        List<UserDto> users = new ArrayList<>();
        for (AcademicDto academicDto : acas){
            List<DocumentDto> documentByAca = documentService.getDocumentByAca(academicDto.getACA_ID());
            UserDto user = userService.getUserById(academicDto.getACA_CREATOR());
            if(documentByAca != null){
                docs.addAll(documentByAca);
            }
            if(user != null && !users.contains(user)){
                users.add(user);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("docs",docs);
        map.put("users",users);
        map.put("count",page.getResult().size());
        page.setParams(map);
        PageSupport.loadGridHtml("/module/base/search/include/list", page);
        return new JsonResult(page);
    }

    @RequestMapping("/content")
    public ModelAndView toContent(String searchType,String value) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/search/content");
        return model;
    }

    @RequestMapping("/detail")
    public ModelAndView toDetail(@RequestParam String id) {
        ModelAndView model = new ModelAndView();
        AcademicDto academic = academicService.getAcademicById2(id);
        academic.setACA_COUNT(Integer.parseInt(academic.getACA_COUNT())+1+"");
        academicService.updateObject(academic);
        academic.setACA_CREATOR(userService.getUserById(academic.getACA_CREATOR()).getUser_name());
        model.addObject("academic",academic);
        List<DocumentDto> documents = documentService.getDocumentByAca(academic.getACA_ID());
        Integer stages = documentService.getStageCount(academic.getACA_ID());
        model.addObject("docCount",documents.size());
        model.addObject("stageCount",stages);
        model.setViewName("/module/base/search/include/detail");
        return model;
    }

    @RequestMapping("/applyKey")
    @ResponseBody
    public JsonResult applyKey(@RequestParam String acaId){
        return searchService.applyKey(acaId);
    }

}
