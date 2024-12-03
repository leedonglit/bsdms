package com.isec.base.monit.dao;

import com.core.security.database.jdbc.BaseDao;
import com.isec.base.monit.dto.GroupDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDao extends BaseDao<GroupDto,String> {

    public List<GroupDto> getGroupsByAca(String acaId){
        return getList("SELECT A.GP_ID,A.GP_ROLE,B.USER_NAME GP_USER FROM CAS_BMDMS_GROUP_TAB A,IDGAR_USER_TAB B WHERE A.ACA_ID = ? AND B.USER_ID = A.GP_USER ORDER BY A.GP_ROLE DESC,A.GP_ADD_TIME ASC",acaId);
    }

}
