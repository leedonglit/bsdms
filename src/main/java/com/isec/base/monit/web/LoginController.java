package com.isec.base.monit.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.core.tools.AppUserTool;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.isec.base.monit.dto.UserDto;
import com.isec.base.monit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("")
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/noLogin");
        return model;
    }

    @RequestMapping("/terms")
    public ModelAndView terms() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/terms");
        return model;
    }

    @RequestMapping("/rest")
    @ResponseBody
    public int rest(HttpServletRequest request) {
        String email = request.getParameter("email");
        String eamilCode = request.getParameter("eamilCode");
        if(!userService.checkEmailCode(eamilCode, email)){
            return 1;
        }
        String password = request.getParameter("password");
        boolean b = userService.resetPwd(password, email);
        if(!b){
            return 2;
        }
        return 0;
    }

    /**
     * 注册
     * @return
     * @return
     */
    @RequestMapping("/regis")
    @ResponseBody
    public int regis(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String eamilCode = request.getParameter("eamilCode");
        if(!userService.checkEmailCode(eamilCode, email)){
            return 1;
        }
        String password = request.getParameter("password");
        UserDto user = new UserDto();
        user.setUser_email(email);
        user.setUser_name(userName);
        user.setUser_id(IdUtil.fastSimpleUUID());
        user.setUser_no(IdUtil.getSnowflakeNextIdStr());
        user.setUser_ip(ServletUtil.getClientIP(request));
        user.setUser_org(request.getParameter("company"));
        user.setUser_interests("");
        user.setUser_page("");
        user.setUser_pwd(password);
        user.setUser_nameimg("");
        userService.register(user) ;
        return 0;//注册成功
    }

    @RequestMapping("/send")
    @ResponseBody
    public boolean send(HttpServletRequest request) {
        String email = request.getParameter("email");
        return userService.sendEmail(email);
    }

    @RequestMapping("/openLogin/{t}")
    public ModelAndView openLogin(@PathVariable("t") String t) {
        ModelAndView model = new ModelAndView();
        model.addObject("from",t);
        model.setViewName("/module/base/account/include/login");
        return model;
    }

    @RequestMapping("/forgotPwd")
    public ModelAndView forgotPwd() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/forgotPwd");
        return model;
    }

    @RequestMapping("/openRegister")
    public ModelAndView openRegister() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/account/include/register");
        return model;
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        AppUserTool.removeAppUser();
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/home");
        return model;
    }

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 验证码刷新
     * @param request
     * @param response
     */
    @GetMapping(value = "/captcha", produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        //-------------------生成验证码 begin --------------------------
        //获取验证码文本内容
        String text = defaultKaptcha.createText();
        //将验证码文本内容放入session
        request.getSession().setAttribute("captcha", text);
        //根据文本验证码内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片，格式为jpg
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Autowired
    UserService userService;

    /**
     * 检查用户邮箱是否已经存在
     * @param request email
     * @return false 不存在 true 已存在
     */
    @RequestMapping("/emailIsExist")
    @ResponseBody
    public boolean emailIsExist(HttpServletRequest request) {
        String email = request.getParameter("email");
        return userService.emailIsExist(email);
    }


    /**
     * 登录  ： 0 暂未审核  1 正常登录 2 被锁定 3用户名或密码错误 4 验证码错误
     * @return 0成功1验证码错误
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public int doLogin(HttpServletRequest request) {
        String verificationCode = request.getParameter("v");
        String captcha = request.getSession().getAttribute("captcha").toString();
        if (StringUtils.isEmpty(verificationCode)||!captcha.equalsIgnoreCase(verificationCode)){
            return 4;//验证码错误
        }
        String userId = request.getParameter("u");
        String pwd = request.getParameter("p");
        return userService.checkLogin(userId, pwd, ServletUtil.getClientIP(request));
    }

}
