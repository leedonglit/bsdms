package com.core.config;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.core.security.filter.LoginInterceptor;
import com.core.tools.PropertiesTool;

@Controller
@RequestMapping("/")
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册TestInterceptor拦截器
		InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
		registration.addPathPatterns("/**"); //所有路径都被拦截
		String patters = PropertiesTool.getString("noFilter.patters");
		registration.excludePathPatterns(Arrays.asList(patters.split(",")));
	}

	String[] pages = new String[]{
			"search","tutorial","contact","about","login","register"};

	@RequestMapping("/{page}")
	public ModelAndView index(@PathVariable String page)  {
		ModelAndView view = new ModelAndView();
		if (Arrays.asList(pages).contains(page)) {
			view.addObject("page",page);
			view.setViewName("module/base/" + page+"/index");
		}else {
			view.addObject("page","home");
			view.setViewName(PropertiesTool.getString("default.page"));
		}
		return view;
	}

	@RequestMapping("/")
	public ModelAndView index()  {
		ModelAndView view = new ModelAndView();
		view.setViewName("module/base/role");
		return view;
	}

	@RequestMapping("/index/toRegister")
	public ModelAndView indexToRegister(){
		ModelAndView view = new ModelAndView();
		view.setViewName(PropertiesTool.getString("default.page"));
		view.addObject("toRegister", "toRegister");
		return view;
	}
}
