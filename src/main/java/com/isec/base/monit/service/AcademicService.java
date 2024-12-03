package com.isec.base.monit.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.common.EncryptUtil;
import com.common.FileUtil;
import com.common.ZipUtil;
import com.core.security.page.AutoPage;
import com.core.tools.AppUserTool;
import com.core.util.Sha256;
import com.isec.base.monit.dao.AcademicDao;
import com.isec.base.monit.dao.DocumentDao;
import com.isec.base.monit.dao.GroupDao;
import com.isec.base.monit.dao.PhaseDao;
import com.isec.base.monit.dto.AcademicDto;
import com.isec.base.monit.dto.DocumentDto;
import com.isec.base.monit.dto.GroupDto;
import com.isec.base.monit.dto.PhaseDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AcademicService {

    @Autowired
    AcademicDao academicDao;

    @Autowired
    AcaLogsService acaLogsService;

    @Autowired
    PhaseDao phaseDao;

    @Autowired
    GroupDao groupDao;

    public AutoPage<AcademicDto> loadAcademic(AutoPage<AcademicDto> page){
        page.addCondition("ACA_CREATOR","=", AppUserTool.getAppUser().getUserID());
        page.addCondition("ACA_ISDEL","=","0");
        return academicDao.findAutoPage(page);
    }

    public AutoPage<AcademicDto> loadParticipated(AutoPage<AcademicDto> page){
        return academicDao.loadParticipated(page);
    }

    public AutoPage<AcademicDto> searchAca(AutoPage<AcademicDto> page){
        return academicDao.findAutoPage(page);
    }

    public void createProject(String pro,String phase){
        pro = URLUtil.decode(pro, Charset.defaultCharset());
        phase = URLUtil.decode(phase, Charset.defaultCharset());
        List<PhaseDto> phases = JSONUtil.toList(phase,PhaseDto.class);
        AcademicDto academicDto = JSONUtil.toBean(pro, AcademicDto.class);
        academicDto.setACA_ISDEL("0");
        academicDto.setACA_STATUS("0");
        if("".equals(academicDto.getACA_ID())){
            academicDto.setACA_CREATOR(AppUserTool.getAppUser().getUserID());
            academicDto.setACA_CREATE_TIME(new Date());
            academicDto.setACA_ID(IdUtil.fastSimpleUUID());
            academicDto.setACA_COUNT("0");
            academicDto.setACA_ORG(userService.getUserById(AppUserTool.getAppUser().getUserID()).getUser_org());
            academicDao.saveObject(academicDto);
        }else{
            AcademicDto oldDto = academicDao.getObject(academicDto.getACA_ID());
            academicDto.setACA_COUNT(oldDto.getACA_COUNT());
            academicDto.setACA_CREATE_TIME(oldDto.getACA_CREATE_TIME());
            academicDto.setACA_CREATOR(oldDto.getACA_CREATOR());
            academicDto.setACA_ORG(oldDto.getACA_ORG());
            academicDao.updateObject(academicDto);
        }
        for (int i = 0; i < phases.size(); i++) {
            phases.get(i).setACA_ID(academicDto.getACA_ID());
            phases.get(i).setPHASE_ID(IdUtil.fastSimpleUUID());
            phases.get(i).setPHASE_ISDEL(0);
        }
        deletePhaseByACA_ID(academicDto.getACA_ID());
        phaseDao.saveAll(phases);
        GroupDto groupDto = new GroupDto();
        groupDto.setGP_ID(IdUtil.fastSimpleUUID());
        groupDto.setGP_ADD_TIME(new Date());
        groupDto.setGP_USER(AppUserTool.getAppUser().getUserID());
        groupDto.setGP_ROLE(2);
        groupDto.setACA_ID(academicDto.getACA_ID());
        groupDao.saveObject(groupDto);
        acaLogsService.saveAcaLogs("Project Dynamics",String.format("<p>%s Created a project:[%s]%s</p>",AppUserTool.getAppUser().getUserName(),academicDto.getACA_CODE(),academicDto.getACA_TITLE()),academicDto.getACA_ID());
    }

    @Autowired
    UserService userService;

    public AcademicDto getAcaById(String acaId){
        AcademicDto academicDto = academicDao.getObject(acaId);
        return academicDto;
    }

    public AcademicDto getAcademicById(String acaId){
        AcademicDto academicDto = academicDao.getObject(acaId);
        academicDto.setACA_CREATOR(userService.getUserById(academicDto.getACA_CREATOR()).getUser_name());
        return academicDto;
    }

    public AcademicDto getAcademicById2(String acaId){
        AcademicDto academicDto = academicDao.getObject(acaId);
        return academicDto;
    }


    public List<PhaseDto> getPhaseByAca(String acaId){
        return phaseDao.getPhaseByAca(acaId);
    }

    @Autowired
    Environment environment;

    public List<Map<String, String>> upload(MultipartFile[] files,String aca){
        List<Map<String, String>> uploadRes = FileUtil.batchUpload(files,environment.getProperty("academic.local.upload.path"));
        DocumentDto documentDto ;
        for (int i = 0; i < uploadRes.size(); i++) {
            documentDto = documentDao.getDocumentDtoByName(aca,uploadRes.get(i).get("fileName"));
            if (null == documentDto){
                uploadRes.get(i).put("version","1");
            }else{
                uploadRes.get(i).put("version",Integer.parseInt(documentDto.getDOC_VERSION())+1+"");
            }
        }
        return uploadRes;
    }

//    public static void main(String[] args) {
//        //888d637b04f38f707a8abc23a4de1510715f3c20ac5e1ce956d2559a07cbab3f
//        System.out.println(FileUtil.getFileHash(new File("\\IntelliJ IDEA 2022.2.3\\workspace\\Academic\\upload\\20241121\\20940fe3-b0fe-4e6c-ac2d-4508d0ba5999.pdf")));
//    }

    public String createLink(String acaId,String id,int failureTime){
        try{
            JSONObject param = new JSONObject();
            param.set("id",id);
            param.set("ACA_ID",acaId);
            param.set("failureTime",new Date().getTime()/1000+failureTime);
            param.set("creator",AppUserTool.getAppUser().getUserID());
            param.set("shareUser",AppUserTool.getAppUser().getUserName());
            String baseParam = Base64.encode(param.toString(),"UTF-8");
            academicDao.createLink(id,acaId,param.toString(), baseParam ,failureTime,1);
            return environment.getProperty("academic.local.baseUrl") + "/academic/addGroup?param="+ baseParam;//返回加密后的参数
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public List<Map<String,Object>> loadKey(String acaId){
        return academicDao.loadKey(acaId,2);
    }

    public boolean saveKey(String acaId,String key,int failureTime,String desc_content,String docIds){
        try{
            JSONObject param = new JSONObject();
            param.set("key",key);
            param.set("ACA_ID",acaId);
            if (failureTime == 1){
                param.set("failureTime",new Date().getTime()/1000+86400);
            }
            if (failureTime == 2){
                param.set("failureTime",new Date().getTime()/1000+604800);
            }
            if (failureTime == 3){
                param.set("failureTime",new Date().getTime()/1000+2592000);
            }
            if (failureTime == 4){
                param.set("failureTime",new Date().getTime()/1000+7776000);
            }
            param.set("creator",AppUserTool.getAppUser().getUserID());
            param.set("shareUser",AppUserTool.getAppUser().getUserName());
            param.set("desc_content",desc_content);
            param.set("terminal", DateTime.of(param.getLong("failureTime")*1000).toString(DatePattern.NORM_DATETIME_FORMAT));
            if(StringUtils.isNotEmpty(docIds)){
                param.set("docIds", Arrays.asList(docIds.split(",")));
            }
            String secret = EncryptUtil.aesEncrypt(param.toString(),"dmbms_2024");
            academicDao.createLink(key,acaId,param.toString(), secret ,failureTime,2);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean delKey(String key){
        try{
            academicDao.delKey(key);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getKeyById(String id) {
        Map<String,Object> map = academicDao.getKeyById(id);
        if (null != map && map.size() > 0){
            return JSONUtil.parseObj(map.get("LK_PARAM"));
        }
        return null;
    }

    public JSONArray getDownloadHistory(String key,String aca) {
        List<Map<String, Object>> map = academicDao.getDownloadHistory(key,aca);
        if (null != map && map.size() > 0){
            return JSONUtil.parseArray(map);
        }
        return new JSONArray();
    }

    public Integer getCounts(int type){
        if (1 == type){
            int count = academicDao.getInt("SELECT COUNT(*) FROM CAS_BMDMS_ACADEMIC_TAB WHERE ACA_ISDEL = 0 and ACA_CREATOR = ? ",AppUserTool.getAppUser().getUserID());
            return count;
        }else{
            //int count = academicDao.getInt("SELECT COUNT(*) FROM CAS_BMDMS_GROUP_TAB WHERE GP_USER = ? ",AppUserTool.getAppUser().getUserID());
            int count = academicDao.getInt("SELECT COUNT(*) FROM CAS_BMDMS_ACADEMIC_TAB WHERE ACA_ID IN(SELECT ACA_ID FROM CAS_BMDMS_GROUP_TAB G WHERE G.GP_USER = '"+AppUserTool.getAppUser().getUserID()+"' AND GP_ROLE != 2) AND ACA_ISDEL = 0 ");
            return count;
        }
    }

    @Autowired
    DocumentDao documentDao;

    public void saveDown(HttpServletResponse response,String id){
        Map<String,Object> downInfo = academicDao.getDownloadById(id);
        String basePath = environment.getProperty("academic.local.download.path") + downInfo.get("DH_ZIP_PATH");
        FileUtil.downloadFile(response,basePath,downInfo.get("DH_KEY")+".zip");
    }

    @Transactional
    public void saveDown(HttpServletResponse response, String password, String eContact, String eSign, String key,String ids){
        Map<String,Object> keyInfo = academicDao.getKeyById(key);
        AcademicDto academicDto = academicDao.getObject(keyInfo.get("ACA_ID")+"");
        List<DocumentDto> docs = documentDao.findByProperty("ACA_ID",academicDto.getACA_ID());
        // 过滤未选中文档
        docs = docs.stream().filter(d -> ids.contains(d.getDOC_ID())).collect(Collectors.toList());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePath = simpleDateFormat.format(new Date());
        String basePath = environment.getProperty("academic.local.download.path") + "/"+datePath+"/"+key+".zip";
        String upPath = environment.getProperty("academic.local.upload.path");
        File[]files = new File[docs.size()];
        String fileName = "";
        File renameFile ;
        File oFile;
        for (int i = 0; i < docs.size(); i++) {
            oFile = FileUtil.decryptFile(upPath+docs.get(i).getDOC_FILE_PATH());//现将文件进行解密，然后下载解密后的文件
            renameFile = new File(oFile.getParent()+File.separator+IdUtil.fastSimpleUUID());
            renameFile.mkdirs();
            renameFile = new File(renameFile.getAbsoluteFile()+File.separator+"(v"+docs.get(i).getDOC_VERSION()+")"+docs.get(i).getDOC_TITLE());
            oFile.renameTo(renameFile);
            files[i] = renameFile;
            fileName+= "☆"+docs.get(i).getDOC_TITLE()+",";
        }
        File targetFile = new File(environment.getProperty("academic.local.download.path"), datePath);
        if (!targetFile.exists()) targetFile.mkdirs();
        ZipUtil.zipEncipher(files,basePath,password);
        for (int i = 0; i < files.length; i++) {
            files[i].deleteOnExit();
        }
        //保存下载日志
        acaLogsService.saveAcaLogs("Download",String.format("<p>Document Name：%s</p><p>Password:%s</p><p>Document:[%s]</p>",AppUserTool.isLogin()?AppUserTool.getAppUser().getUserName():key,password,fileName),academicDto.getACA_ID());
        String docId = IdUtil.fastSimpleUUID();
        String eHash = documentDao.saveElectronic(docId,"1",eContact,eSign);
        String hash = Sha256.getSHA256Str(String.format("%s%s%s%s%s%s%s%s",
                JSONUtil.toJsonStr(docs),
                key,
                "/"+datePath+"/"+key+".zip",
                password,
                academicDto.getACA_ID(),
                AppUserTool.isLogin()?AppUserTool.getAppUser().getUserID():"",
                AppUserTool.isLogin()?AppUserTool.getAppUser().getUserName():"",
                eHash));
        academicDao.excute("INSERT INTO CAS_BMDMS_DOWNLOAD_TAB(DH_ID,DH_TIME,DH_FILES,DH_KEY,DH_ZIP_PATH,DH_ZIP_PASSWORD,ACA_ID,USER_ID,USER_NAME,DATA_HASH) " +
                        "value(?,NOW(),?,?,?,?,?,?,?,?)",
                docId,
                JSONUtil.toJsonStr(docs),
                key,
                "/"+datePath+"/"+key+".zip",
                password,
                academicDto.getACA_ID(),
                AppUserTool.isLogin()?AppUserTool.getAppUser().getUserID():"",
                AppUserTool.isLogin()?AppUserTool.getAppUser().getUserName():"",
                hash);
        //执行文件下载
        FileUtil.downloadFile(response,basePath,key+".zip");
    }


    public void deletePhase(String phaseId) {
        String sql = "update cas_bmdms_phase_tab set PHASE_ISDEL = '1' where PHASE_ID = '"+phaseId+"'";
        phaseDao.excute(sql);
    }
    public void deletePhaseByACA_ID(String acaId) {
        String sql = "update cas_bmdms_phase_tab set PHASE_ISDEL = '1' where ACA_ID = '"+acaId+"'";
        phaseDao.excute(sql);
    }

    public void projectdeleteBatch(String ids) {
        String[] split = ids.split(",");
        for (String acaId : split){
            String sql = "update cas_bmdms_academic_tab set ACA_ISDEL = '1' where ACA_ID = '"+acaId+"'";
            academicDao.excute(sql);
        }
    }

    public List<Map<String, Object>> getLimitAcademicClass() {
        String sql = "SELECT" +
                " ll.ACA_CLASS " +
                " FROM" +
                " (SELECT * FROM cas_bmdms_academic_tab WHERE ACA_ISDEL = '0' ORDER BY ACA_CREATE_TIME DESC ) as ll GROUP BY ll.ACA_CLASS" +
                " LIMIT 0,5";
        return academicDao.getMapList(sql);
    }

    public List<Map<String, Object>> getLimitAcademicAuthor() {
        String sql = "SELECT" +
                " user_name,user_id" +
                " FROM" +
                " idgar_user_tab" +
                " WHERE" +
                " user_id IN ( SELECT ll.ACA_CREATOR AS user_id FROM ( SELECT ACA_CREATOR FROM cas_bmdms_academic_tab WHERE ACA_ISDEL = '0' ORDER BY ACA_CREATE_TIME DESC LIMIT 0, 5 ) AS ll );";
        return academicDao.getMapList(sql);
    }

    public List<Map<String, Object>> getLimitAcademicOrg() {
        String sql = "SELECT" +
                " ll.ACA_ORG " +
                " FROM" +
                " (SELECT * FROM cas_bmdms_academic_tab WHERE ACA_ISDEL = '0' ORDER BY ACA_CREATE_TIME DESC ) as ll GROUP BY ll.ACA_ORG" +
                " LIMIT 0,5";
        return academicDao.getMapList(sql);
    }

    public List<Map<String, Object>> getLimitAcademicYears() {
        // 查询字段的年份
        String sql = "SELECT" +
                " YEAR(ACA_CREATE_TIME) AS ACA_YEARS " +
                " FROM" +
                " cas_bmdms_academic_tab " +
                " WHERE" +
                " ACA_ISDEL = '0' " +
                " GROUP BY" +
                " YEAR(ACA_CREATE_TIME)" +
                " ORDER BY" +
                " YEAR(ACA_CREATE_TIME) DESC LIMIT 0,5";
        return academicDao.getMapList(sql);
    }

    public void updateObject(AcademicDto academic) {
        academicDao.updateObject(academic);
    }
}
