package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_academic_tab", pkey= "ACA_ID")
public class AcademicDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name="ACA_ID")
    private String ACA_ID;//

    @javax.persistence.Column(name="ACA_TITLE")
    private String ACA_TITLE;//项目名称

    @javax.persistence.Column(name="ACA_CODE")
    private String ACA_CODE;//项目编号

    @javax.persistence.Column(name="ACA_CLASS")
    private String ACA_CLASS;//所属领域/行业

    @javax.persistence.Column(name="ACA_ORG")
    private String ACA_ORG;//所属组织

    @javax.persistence.Column(name="ACA_START_TIME")
    private Date ACA_START_TIME;//项目开始时间

    @javax.persistence.Column(name="ACA_END_TIME")
    private Date ACA_END_TIME;//项目结束时间

    @javax.persistence.Column(name="ACA_DESC")
    private String ACA_DESC;//项目描述

    @javax.persistence.Column(name="ACA_ISDEL")
    private String ACA_ISDEL;//删除标记

    @javax.persistence.Column(name="ACA_STATUS")
    private String ACA_STATUS;//项目状态

    @javax.persistence.Column(name="ACA_CREATOR")
    private String ACA_CREATOR;//创建人

    @javax.persistence.Column(name="ACA_CREATE_TIME")
    private Date ACA_CREATE_TIME;//创建时间

    @javax.persistence.Column(name="ACA_COUNT")
    private String ACA_COUNT;//访问次数
}
