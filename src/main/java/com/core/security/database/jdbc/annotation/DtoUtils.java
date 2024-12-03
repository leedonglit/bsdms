package com.core.security.database.jdbc.annotation;

import com.core.security.bean.BeanReflactUtil;
 
public class DtoUtils {
	private Class<?> annotationType;
	private String select;
	private String tableName;
	private String pkey;
	private String pkeyFiled;
	private String orderby;
	private String[] ucolumns;
	private String pid;
	boolean numAutoKey;

	
	
	
	public static DtoUtils getDtoInfo(Class<?> clazz) {
		if (clazz == Object.class)
			return null;
		if (clazz.isAnnotationPresent(DTO.class)) {
			DTO dto = (DTO) clazz.getAnnotation(DTO.class);
			DtoUtils dtoinfo = new DtoUtils();
			dtoinfo.setAnnotationType(clazz);
			dtoinfo.setNumAutoKey(dto.isNumAutoKey());
			dtoinfo.setOrderby(dto.orderby());
			dtoinfo.setPid(dto.pid());
			String pk=BeanReflactUtil.getPkColumnName(clazz);
			if(pk.equals("")){
				dtoinfo.setPkey(dto.pkey());
			}else{
				dtoinfo.setPkey(pk);
			}
			dtoinfo.setTableName(dto.tableName());
			dtoinfo.select = dto.select();
			dtoinfo.setPkeyFiled(BeanReflactUtil.getPkFiledName(clazz));
			return dtoinfo;
		}
		// ToolsLog.info(clazz.getSuperclass().getName());
		return getDtoInfo(clazz.getSuperclass());
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public Class<?> annotationType() {
		return annotationType;
	}

	public void setAnnotationType(Class<?> annotationType) {
		this.annotationType = annotationType;
	}

	public String tableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String pkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public String orderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String[] ucolumns() {
		return ucolumns;
	}

	public void setUcolumns(String[] ucolumns) {
		this.ucolumns = ucolumns;
	}

	public String pid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public boolean isNumAutoKey() {
		return numAutoKey;
	}

	public void setNumAutoKey(boolean numAutoKey) {
		this.numAutoKey = numAutoKey;
	}

	public String select() {
		return select;
	}

	public String pkeyFiled() {
		return pkeyFiled;
	}
	public void setPkeyFiled(String pkeyFiled) {
		this.pkeyFiled = pkeyFiled;
	}
}
