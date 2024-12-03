package com.core.extend.attachment.dto;

import java.util.Date;

import com.core.tools.DBTool;

@com.core.security.database.jdbc.annotation.DTO(tableName = "sys_adjunct", pkey = "ADJID")
public class Adjunct implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@javax.persistence.Column(name = "ADJID")
	private String ADJID;//

	@javax.persistence.Column(name = "ADJSHOWNAME")
	private String ADJSHOWNAME;//

	@javax.persistence.Column(name = "ADJTRUENAME")
	private String ADJTRUENAME;//

	@javax.persistence.Column(name = "ADJTYPE")
	private String ADJTYPE;//

	@javax.persistence.Column(name = "ADJDOWNURL")
	private String ADJDOWNURL;//

	@javax.persistence.Column(name = "ADJSIZE")
	private Integer ADJSIZE;//

	@javax.persistence.Column(name = "CREATETIME")
	private Date CREATETIME;//

	@javax.persistence.Column(name = "OTHERID")
	private String OTHERID;//

	@javax.persistence.Column(name = "FLASHURL")
	private String FLASHURL;//

	public String getADJID() {
		return ADJID;
	}

	public void setADJID(String ADJID) {
		this.ADJID = ADJID;
	}

	public String getADJSHOWNAME() {
		return ADJSHOWNAME;
	}

	public void setADJSHOWNAME(String ADJSHOWNAME) {
		this.ADJSHOWNAME = ADJSHOWNAME;
	}

	public String getADJTRUENAME() {
		return ADJTRUENAME;
	}

	public void setADJTRUENAME(String ADJTRUENAME) {
		this.ADJTRUENAME = ADJTRUENAME;
	}

	public String getADJTYPE() {
		return ADJTYPE;
	}

	public void setADJTYPE(String ADJTYPE) {
		this.ADJTYPE = ADJTYPE;
	}

	public String getADJDOWNURL() {
		return ADJDOWNURL;
	}

	public void setADJDOWNURL(String ADJDOWNURL) {
		this.ADJDOWNURL = ADJDOWNURL;
	}

	public Integer getADJSIZE() {
		return ADJSIZE;
	}

	public void setADJSIZE(Integer ADJSIZE) {
		this.ADJSIZE = ADJSIZE;
	}

	public Date getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Date CREATETIME) {
		this.CREATETIME = CREATETIME;
	}

	public String getOTHERID() {
		return OTHERID;
	}

	public void setOTHERID(String OTHERID) {
		this.OTHERID = OTHERID;
	}

	public String getFLASHURL() {
		return FLASHURL;
	}

	public void setFLASHURL(String FLASHURL) {
		this.FLASHURL = FLASHURL;
	}

}
