package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;
@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "igdar_datalog_tab", pkey= "dataId")
public class DataLogDto implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	@javax.persistence.Column(name="dataId")
	private String dataId;//数据ID

	@javax.persistence.Column(name="dataType")
	private String dataType;//数据类型1pm2source

	@javax.persistence.Column(name="dataUptime")
	private Date dataUptime;//数据上传时间

	@javax.persistence.Column(name="dataHash")
	private String dataHash;//数据对应hash值

	@javax.persistence.Column(name="dataUpuser")
	private String dataUpuser;//

	@javax.persistence.Column(name="dataIndex")
	private Integer dataIndex;//数据对应下标
	
	@javax.persistence.Column(name="dataDowncount")
	private Integer dataDowncount;//数据对应下标
}
