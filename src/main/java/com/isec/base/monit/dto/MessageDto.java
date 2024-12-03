package com.isec.base.monit.dto;

import com.core.tools.DBTool;
import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_message_tab", pkey= "MSG_ID")
public class MessageDto implements java.io.Serializable{

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name="MSG_ID")
    private String MSG_ID;//

    @javax.persistence.Column(name="MSG_FROM")
    private String MSG_FROM;//

    @javax.persistence.Column(name="MSG_TITLE")
    private String MSG_TITLE;//

    @javax.persistence.Column(name="MSG_CONTENT")
    private String MSG_CONTENT;//

    @javax.persistence.Column(name="MSG_TIME")
    private Date MSG_TIME;//

    @javax.persistence.Column(name="MSG_LEVEL")
    private Integer MSG_LEVEL;//

    @javax.persistence.Column(name="MSG_ISDEL")
    private Integer MSG_ISDEL;//

    @javax.persistence.Column(name="MSG_SENDER")
    private String MSG_SENDER;//

    @javax.persistence.Column(name="MSG_RECEIVER")
    private String MSG_RECEIVER;//

    @javax.persistence.Column(name="MSG_FOREIGN")
    private String MSG_FOREIGN;//

}
