package com.isec.base.monit.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author zhangxuhui
 * @Date 2024/8/29
 * @email zxh_1633@163.com
 */
@RequestMapping("/contact")
@Controller
public class ContactController {

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/contact/index");
        return model;
    }

}
