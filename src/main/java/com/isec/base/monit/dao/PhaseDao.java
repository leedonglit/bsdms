package com.isec.base.monit.dao;

import com.core.security.database.jdbc.BaseDao;
import com.isec.base.monit.dto.PhaseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhaseDao extends BaseDao<PhaseDto,String> {

    public List<PhaseDto> getPhaseByAca(String acaId){
        return findByProperty("PHASE_ISDEL = 0 AND ACA_ID",acaId);
    }

}
