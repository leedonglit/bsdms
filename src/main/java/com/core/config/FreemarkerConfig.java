package com.core.config;

import java.lang.annotation.Annotation;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import freemarker.template.TemplateModelException;

@ControllerAdvice
@Configuration
public class FreemarkerConfig {


	@Autowired
	FreeMarkerViewResolver freeMarkerViewResolver;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	FreeMarkerConfigurer freeMarkerConfigurer;

	@PostConstruct
	public void freeMarkerConfigurer() throws BeansException, TemplateModelException {
		freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();
		String []beans = applicationContext.getBeanDefinitionNames();
		for (String bean : beans) {
			String serviceAnnotationClass = "interface org.springframework.stereotype.Service";
			Annotation[] annotations = applicationContext.getBean(bean).getClass().getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().toString().equals(serviceAnnotationClass)) {
					configuration.setSharedVariable(bean, applicationContext.getBean(bean));
				}
			}
		}
	}


	@Autowired
	Environment environment;

	@ModelAttribute(name="ctxPath")
	public String setConfiguration() throws TemplateModelException {
		String ctxPath = environment.getProperty("server.servlet.context-path");
		if (StringUtils.isEmpty(ctxPath)) {
			return "";
		}
		return ctxPath;
	}
}
