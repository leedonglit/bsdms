package com.isec.base.monit.dto;

import com.core.tools.DBTool;
import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_document_tab", pkey= "DOC_ID")
public class DocumentDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name="DOC_ID")
    private String DOC_ID;//

    @javax.persistence.Column(name="DOC_TITLE")
    private String DOC_TITLE;//文档标题

    @javax.persistence.Column(name="DOC_TYPE")
    private String DOC_TYPE;//文档类型（后缀）

    @javax.persistence.Column(name="DOC_UPLOAD_TIME")
    private String DOC_UPLOAD_TIME;//上传时间

    @javax.persistence.Column(name="DOC_SIZE")
    private String DOC_SIZE;//大小(kb)

    @javax.persistence.Column(name="DOC_DESC")
    private String DOC_DESC;//描述

    @javax.persistence.Column(name="DOC_STATUS")
    private String DOC_STATUS;//状态0正常1删除2更新

    @javax.persistence.Column(name="DOC_VERSION")
    private String DOC_VERSION;//文档版本

    @javax.persistence.Column(name="DOC_USER")
    private String DOC_USER;//文档上传者

    @javax.persistence.Column(name="DOC_FILE_NAME")
    private String DOC_FILE_NAME;//文档实际名称

    @javax.persistence.Column(name="DOC_FILE_PATH")
    private String DOC_FILE_PATH;//文档存储路径

    @javax.persistence.Column(name="DOC_FILE_REALNAME")
    private String DOC_FILE_REALNAME;//文档存储名

    @javax.persistence.Column(name="DOC_ORIGINAL")
    private String DOC_ORIGINAL;//文档引用来源

    @javax.persistence.Column(name="DOC_CONTENT")
    private String DOC_CONTENT;//文档内容

    @javax.persistence.Column(name="Kinds")
    private String Kinds;//

    @javax.persistence.Column(name="Acquired")
    private String Acquired;//

    @javax.persistence.Column(name="AMethods")
    private String AMethods;//

    @javax.persistence.Column(name="ALocation")
    private String ALocation;//

    @javax.persistence.Column(name="SLocation")
    private String SLocation;//

    @javax.persistence.Column(name="ACA_ID")
    private String ACA_ID;//

    @javax.persistence.Column(name="DATA_HASH")
    private String DATA_HASH;//

}
