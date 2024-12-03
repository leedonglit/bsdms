package com.isec.base.monit.dao;

import com.core.security.database.jdbc.BaseDao;
import com.core.security.page.AutoPage;
import com.core.security.page.ColDefine;
import com.core.tools.AppUserTool;
import com.isec.base.monit.dto.AcademicDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AcademicDao extends BaseDao<AcademicDto,String> {


    public void createLink(String id,String acaId,String param,String secret,int failureTime,int type){
        this.excute("DELETE FROM CAS_BMDMS_LINK_TAB WHERE LK_ID = ?",id);
        this.excute("INSERT INTO CAS_BMDMS_LINK_TAB(LK_ID,LK_PARAM,LK_SECRET,LK_ISAVAILABLE,LK_FAILURE_TIME,LK_CREATIME,LK_CREATOR,ACA_ID,LK_TYPE) value(?,?,?,1,?,NOW(),?,?,?)",id,param,secret,failureTime, AppUserTool.getAppUser().getUserID(),acaId,type);
    }

    public List<Map<String,Object>> loadKey(String acaId, int type) {
        return this.getMapList("SELECT * FROM CAS_BMDMS_LINK_TAB WHERE LK_TYPE = "+type + " and ACA_ID = '"+acaId+"'");
    }

    public void delKey(String key) {
        this.excute("DELETE FROM CAS_BMDMS_LINK_TAB WHERE LK_ID = ?",key);
    }

    public Map<String,Object> getKeyById(String id) {
        return this.getMap("SELECT * FROM CAS_BMDMS_LINK_TAB WHERE LK_ID = '" + id + "'");
    }

    public Map<String,Object> getDownloadById(String id) {
        return this.getMap("SELECT * FROM CAS_BMDMS_DOWNLOAD_TAB WHERE DH_ID = '" + id + "'");
    }

    public List<Map<String, Object>> getDownloadHistory(String key,String aca) {
        if (AppUserTool.isLogin()){
            if(!StringUtils.isBlank(key)){
                return this.getMapList("SELECT * FROM CAS_BMDMS_DOWNLOAD_TAB WHERE (DH_KEY = '" + key + "' OR USER_ID = '" + AppUserTool.getAppUser().getUserID() + "') AND ACA_ID = '" + aca + "'");
            }
            return this.getMapList("SELECT * FROM CAS_BMDMS_DOWNLOAD_TAB WHERE USER_ID = '" + AppUserTool.getAppUser().getUserID() + "' AND ACA_ID = '" + aca + "'");
        }else{
            if(!StringUtils.isBlank(key)){
                return this.getMapList("SELECT * FROM CAS_BMDMS_DOWNLOAD_TAB WHERE ACA_ID = '" + aca + "'");
            }
            return this.getMapList("SELECT * FROM CAS_BMDMS_DOWNLOAD_TAB WHERE DH_KEY = '" + key + "' AND ACA_ID = '" + aca + "'");
        }
    }


    public AutoPage<AcademicDto> loadParticipated(AutoPage<AcademicDto> page){
        String sql = "SELECT * FROM CAS_BMDMS_ACADEMIC_TAB WHERE ACA_ID IN(SELECT ACA_ID FROM CAS_BMDMS_GROUP_TAB G WHERE G.GP_USER = '"+AppUserTool.getAppUser().getUserID()+"' AND GP_ROLE != 2) AND ACA_ISDEL = 0 ";
        ColDefine[] condition = page.getCondition();
        for (int i = 0; i < condition.length; i++){
            ColDefine colDefine = condition[i];
            if("like".equals(colDefine.getType())){
                sql += " and "+colDefine.getCol()+" like '%"+colDefine.getValue()[0]+"%'";
            }else{
                sql += " and "+colDefine.getCol()+" "+colDefine.getType()+" '"+colDefine.getValue()[0]+"'";
            }
        }
        sql += " ORDER BY ACA_CREATE_TIME";
        return this.findAutoPageBySQL(page,sql);
    }

}
