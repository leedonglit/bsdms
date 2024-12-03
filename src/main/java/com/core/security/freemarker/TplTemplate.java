package com.core.security.freemarker;

import java.io.IOException;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;


public class TplTemplate extends FtlSupport{
	 
	public String process(Map<String, Object> rootMap, String templateName) {
		String content = null;
		try {
			Template template = cfg.getTemplate(templateName);
			java.io.CharArrayWriter  out=new java.io.CharArrayWriter ();	
			template.process(rootMap,out);		
			content = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return content;
	} 
	 
}
