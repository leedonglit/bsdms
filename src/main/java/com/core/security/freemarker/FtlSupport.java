package com.core.security.freemarker;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class FtlSupport {
	
	public static Configuration cfg = getConfiguration();
	
	private static Configuration getConfiguration(){
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setDefaultEncoding("UTF-8");
		configurer.setTemplateLoaderPath("classpath:/templates/");
		try {
			return configurer.createConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return  null;
	}
}
