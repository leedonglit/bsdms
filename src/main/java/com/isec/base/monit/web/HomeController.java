package com.isec.base.monit.web;

import com.core.security.page.AutoPage;
import com.core.security.page.JsonResult;
import com.core.security.page.PageSupport;
import com.isec.base.monit.dto.AcademicDto;
import com.isec.base.monit.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author zhangxuhui
 * @Date 2024/8/29
 * @email zxh_1633@163.com
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    MessageService messageService;

    @RequestMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("/module/base/home/index");
    }

    @RequestMapping("/loadMsg")
    @ResponseBody
    public JsonResult loadMsg(){
        return new JsonResult(messageService.loadMessageByUser());
    }

    @RequestMapping("/role")
    public ModelAndView role(){
        return new ModelAndView("/module/base/role");
    }

}
