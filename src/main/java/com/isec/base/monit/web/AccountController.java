package com.isec.base.monit.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.core.security.page.AutoPage;
import com.core.security.page.JsonResult;
import com.core.security.page.PageSupport;
import com.isec.base.monit.dto.AcademicDto;
import com.isec.base.monit.dto.MessageDto;
import com.isec.base.monit.dto.UserDto;
import com.isec.base.monit.service.AcademicService;
import com.isec.base.monit.service.MessageService;
import com.isec.base.monit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/account")
public class AccountController {


    /**
     * 用户详情
     * userId 用户id
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        String page = request.getParameter("page");
        model.addObject("project_count",academicService.getCounts(1));
        model.addObject("participated_count",academicService.getCounts(2));
        model.addObject("message_count",messageService.getCounts());
        model.addObject("user_count",userService.getCounts());
        model.addObject("page", StringUtils.isEmpty(page)?"-1":page);
        model.setViewName("/module/base/account/index");
        return model;
    }

    @RequestMapping("/loadAccount")
    public ModelAndView account() {
        ModelAndView model = new ModelAndView();
        model.addObject("user",userService.loadUser());
        model.setViewName("/module/base/account/include/account");
        return model;
    }


    @RequestMapping("/myAcademic")
    public ModelAndView myAcademic() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/myAcademic");
        return model;
    }

    @RequestMapping("/participated")
    public ModelAndView participated() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/participated");
        return model;
    }

    @RequestMapping("/message")
    public ModelAndView message() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/message");
        return model;
    }

    @RequestMapping("/manage")
    public ModelAndView manage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/manage");
        return model;
    }

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @RequestMapping("/updateUser")
    @ResponseBody
    public JsonResult updateUser(String data){
        JSONObject user = JSONUtil.parseObj(data);
        UserDto selectUser = userService.getUserById(user.getStr("user_id"));
        boolean check = userService.checkEmailCode(user.getStr("user_email_code"),user.getStr("user_email"));
        if(check){
            selectUser.setUser_name(user.getStr("user_name"));
            selectUser.setUser_org(user.getStr("user_org"));
            selectUser.setUser_page(user.getStr("user_page"));
            selectUser.setUser_interests(user.getStr("user_interests"));
            selectUser.setUser_email(user.getStr("user_email"));
            selectUser.setUser_pwd(user.getStr("user_pwd"));
            boolean flag = userService.updateUser(selectUser);
            return new JsonResult(flag);
        }
        return new JsonResult(false);
    }

    @RequestMapping("/deleteBatch")
    @ResponseBody
    public JsonResult deleteBatch(String ids){
        messageService.deleteBatch(ids);
        return new JsonResult("delete success");
    }

    @RequestMapping("/loadAllMsg")
    @ResponseBody
    public JsonResult loadAllMsg(@RequestParam String jsonpage){
        AutoPage<MessageDto> page = PageSupport.fromJsonAutoPage(jsonpage);
        page = messageService.loadAllMsg(page);
        PageSupport.loadGridHtml("/module/base/account/list/msgs", page);
        return new JsonResult(page);
    }

    @RequestMapping("/batchOperationUser")
    @ResponseBody
    public JsonResult batchOperationUser(String ids,String type){
        userService.batchOperationUser(ids,type);
        return new JsonResult("success");
    }

    @RequestMapping("/loadAllUser")
    @ResponseBody
    public JsonResult loadAllUser(@RequestParam String jsonpage){
        AutoPage<UserDto> page = PageSupport.fromJsonAutoPage(jsonpage);
        page = userService.loadAllUser(page);
        PageSupport.loadGridHtml("/module/base/account/list/users", page);
        return new JsonResult(page);
    }

    @Autowired
    AcademicService academicService;

    @RequestMapping("/allocation")
    @ResponseBody
    public JsonResult allocation(@RequestParam String mId,String msg){
        MessageDto messageDto = messageService.loadMessageById(mId);
        AcademicDto academicDto = academicService.getAcaById(messageDto.getMSG_FOREIGN());
        messageService.saveMessage("Allocation for key",String.format("The project you applied to view [%s] has been assigned a KEY by the manager.KEY:%s",academicDto.getACA_TITLE(),msg),messageDto.getMSG_SENDER(),messageDto.getMSG_FOREIGN());
        return  new JsonResult(true);
    }

}
