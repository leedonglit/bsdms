package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_document_tab", pkey= "DOC_ID")
public class ElectronicDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Column(name="FOREIGN_ID")
    private String FOREIGN_ID;

    @javax.persistence.Column(name="Electronic_date")
    private Date DOC_TITLE;

    @javax.persistence.Column(name="Electronic_contract")
    private String Electronic_contract;

    @javax.persistence.Column(name="Electronic_signature")
    private String Electronic_signature;

    @javax.persistence.Column(name="DATA_HASH")
    private String DATA_HASH;

}
