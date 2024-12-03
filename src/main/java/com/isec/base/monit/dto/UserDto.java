package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;
@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "idgar_user_tab", pkey= "user_id")
public class UserDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@javax.persistence.Column(name="user_id")
	private String user_id;//

	@javax.persistence.Column(name="user_no")
	private String user_no;//用户编号

	@javax.persistence.Column(name="user_name")
	private String user_name;//姓名

	@javax.persistence.Column(name="user_pwd")
	private String user_pwd;//密码

	@javax.persistence.Column(name="user_status")
	private String user_status;//状态0锁定1激活

	@javax.persistence.Column(name="user_ip")
	private String user_ip;//用户注册归属地IP

	@javax.persistence.Column(name="user_from")
	private String user_from;//用户来源

	@javax.persistence.Column(name="user_regtime")
	private Date user_regtime;//注册时间

	@javax.persistence.Column(name="user_email")
	private String user_email;//邮箱

	@javax.persistence.Column(name="user_interests")
	private String user_interests;//研究兴趣

	@javax.persistence.Column(name="user_org")
	private String user_org;//所属单位

	@javax.persistence.Column(name="user_page")
	private String user_page;//个人/导师网页
	
	@javax.persistence.Column(name="user_nameimg")
	private String user_nameimg;//用户签名

	@javax.persistence.Column(name="user_isdel")
	private String user_isdel;//
	
	@javax.persistence.Column(name="user_level")
	private Integer user_level;//
	
	@javax.persistence.Column(name="user_hash")
	private String user_hash;//

}
