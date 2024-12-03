package com.core.security.page;

import java.io.CharArrayWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.core.security.freemarker.FtlSupport;
import com.core.security.freemarker.TplTemplate;
import com.core.tools.SpringContextTool;

import cn.hutool.json.JSONUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

public class PageSupport extends FtlSupport{

	public static void loadGridHtml(String ftl,Page<?> page,HttpServletRequest request){
		Map<String,Object> map =  new HashMap<String,Object>();
		map.put("gpage", page);
		map.put("ctxPath", request.getServletContext().getContextPath());
		TplTemplate webTemplate = new TplTemplate();
		String fullhtml;
		try {
			fullhtml = webTemplate.process(map, ftl);
			page.setHtml(fullhtml);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void loadGridHtml(String ftl,Page<?> page){
		Map<String,Object> map =  new HashMap<String,Object>();
		map.put("gpage", page);
		String url = SpringContextTool.context.getEnvironment().getProperty("server.servlet.context-path");
		map.put("ctxPath", url);
		map.put("ctxPath", url);
		TplTemplate webTemplate = new TplTemplate();
		String fullhtml;
		try {
			fullhtml = webTemplate.process(map, ftl.substring(0).concat(".htm"));
			page.setHtml(fullhtml);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> Page<T> fromJsonPage(String jsonpage){
		PageBean pb = JSONUtil.toBean(jsonpage, PageBean.class);
		Page<T> page = new Page<T>(pb.getPageSize(),pb.getPageNo());
		return page;
	}

	@SuppressWarnings("unchecked")
	public static <T> AutoPage<T> fromJsonAutoPage(String jsonpage){
		return  JSONUtil.toBean(jsonpage, AutoPage.class);
	}

	public static Map<String,String> getWhereCondition(String jsonpage){
		Map<String,String> map = new HashMap<String,String>();
		PageBean pb = JSONUtil.toBean(jsonpage, PageBean.class);
		if(null!=pb && null != pb.getCondition()){
			for(Condition c : pb.getCondition()){
				map.put(c.getCol().trim(), c.getValue().trim());
			}
		}
		return map;
	} 

	public static void loadObjHtml(String ftl,AutoPage<?> page){
		Map<String,Object> map =  new HashMap<String,Object>();
		map.put("gpage", page);
		String url = SpringContextTool.context.getEnvironment().getProperty("server.servlet.context-path");
		map.put("ctxPath", url);
		try {
			Template template = new Configuration(new Version(2)).getTemplate(ftl);
			CharArrayWriter  out=new CharArrayWriter ();	
			template.process(map,out);		
			page.setHtml(out.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static String loadObjHtml(String ftl,Map<String,Object> map ){
		String url = SpringContextTool.context.getEnvironment().getProperty("server.servlet.context-path");
		map.put("ctxPath", url);
		TplTemplate webTemplate = new TplTemplate();
		String fullhtml;
		try {
			fullhtml = webTemplate.process(map, ftl.concat(".htm"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fullhtml;
	}
}
