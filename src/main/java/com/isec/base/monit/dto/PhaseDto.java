package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_phase_tab", pkey= "PHASE_ID")
public class PhaseDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name="PHASE_ID")
    private String PHASE_ID;//

    @javax.persistence.Column(name="PHASE_NAME")
    private String PHASE_NAME;//结构名称

    @javax.persistence.Column(name="PHASE_START_TIME")
    private Date PHASE_START_TIME;//阶段开始时间

    @javax.persistence.Column(name="PHASE_END_TIME")
    private Date PHASE_END_TIME;//阶段结束时间

    @javax.persistence.Column(name="PHASE_INDEX")
    private Integer PHASE_INDEX;//阶段次序

    @javax.persistence.Column(name="PHASE_DESC")
    private String PHASE_DESC;//阶段描述

    @javax.persistence.Column(name="PHASE_ISDEL")
    private Integer PHASE_ISDEL;//删除标记

    @javax.persistence.Column(name="ACA_ID")
    private String ACA_ID;//项目主键

}
