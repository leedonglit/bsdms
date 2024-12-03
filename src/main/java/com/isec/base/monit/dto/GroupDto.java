package com.isec.base.monit.dto;

import lombok.Data;

import java.util.Date;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "cas_bmdms_group_tab", pkey= "GP_ID")
public class GroupDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name="GP_ID")
    private String GP_ID;//

    @javax.persistence.Column(name="GP_USER")
    private String GP_USER;//成员ID

    @javax.persistence.Column(name="GP_ROLE")
    private Integer GP_ROLE;//成员角色0普通参与者1管理员2创建者

    @javax.persistence.Column(name="GP_ADD_TIME")
    private Date GP_ADD_TIME;//加入时间

    @javax.persistence.Column(name="ACA_ID")
    private String ACA_ID;
}
