package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_log_tab", pkey= "LOG_ID")
public class AcaLogsDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name="LOG_ID")
    private String LOG_ID;//

    @javax.persistence.Column(name="LOG_TITLE")
    private String LOG_TITLE;//

    @javax.persistence.Column(name="LOG_DESC")
    private String LOG_DESC;//

    @javax.persistence.Column(name="LOG_TIME")
    private Date LOG_TIME;//

    @javax.persistence.Column(name="ACA_ID")
    private String ACA_ID;//
}
