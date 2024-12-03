package com.isec.base.monit.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.common.FileUtil;
import com.core.tools.AppUserTool;
import com.core.util.Sha256;
import com.isec.base.monit.dao.DocumentDao;
import com.isec.base.monit.dto.DocumentDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    DocumentDao documentDao;

    @Autowired
    AcaLogsService acaLogsService;

    @Autowired
    private KkfileViewService kkfileViewService;

    public DocumentDto getDocumentBydocId(String docId){
        return documentDao.getObject(docId);
    }


    public List<DocumentDto> getDocumentByAca(String acaId){
        return documentDao.getList("SELECT DOC_ID,DOC_TITLE,DOC_TYPE,DOC_UPLOAD_TIME,DOC_SIZE,DOC_DESC,DOC_STATUS,DOC_VERSION,DOC_USER,DOC_FILE_PATH,DOC_FILE_NAME,DOC_FILE_REALNAME,DOC_ORIGINAL,DATA_HASH,ACA_ID FROM CAS_BMDMS_DOCUMENT_TAB WHERE ACA_ID = ? ORDER BY DOC_UPLOAD_TIME ASC", acaId);
    }

    public String checkIsNodeUser(String acaId){
        try{
            List<DocumentDto> list = documentDao.getList("SELECT * FROM IDGAR_DBS_TAB WHERE USER_ID = ? AND ACA_ID = ?",AppUserTool.getAppUser().getUserID(), acaId);
            return null == list||list.size() == 0?"1":"0";
        }catch (Exception e){
            return "0";
        }
    }

    public Integer getStageCount(String acaId){
        return documentDao.getInt("SELECT COUNT(1) FROM CAS_BMDMS_PHASE_TAB WHERE ACA_ID = ?", acaId);
    }

    public List<DocumentDto> getDocumentByAca(String acaId,String ids){
        List<DocumentDto> docs = documentDao.getList("SELECT DOC_ID,DOC_TITLE,DOC_TYPE,DOC_UPLOAD_TIME,DOC_SIZE,DOC_DESC,DOC_STATUS,DOC_VERSION,DOC_USER,DOC_FILE_PATH,DOC_FILE_NAME,DOC_FILE_REALNAME,DOC_ORIGINAL,DATA_HASH,ACA_ID FROM CAS_BMDMS_DOCUMENT_TAB WHERE ACA_ID = ? ORDER BY DOC_UPLOAD_TIME ASC", acaId);
        return docs.stream().filter(d -> ids.contains(d.getDOC_ID())).collect(Collectors.toList());
    }

    @Autowired
    Environment environment;

    public void submitDocument(DocumentDto documentDto,String eSign){
        documentDto.setDOC_UPLOAD_TIME(DateTime.now().toMsStr());
        documentDto.setDOC_ID(IdUtil.fastSimpleUUID());
        documentDto.setDOC_STATUS("0");
        documentDto.setDOC_USER(AppUserTool.getAppUser().getUserID());
        documentDto.setDOC_FILE_PATH(URLUtil.decode(documentDto.getDOC_FILE_PATH()));
        documentDto.setDOC_CONTENT(FileUtil.encodeFileToBase64Binary(environment.getProperty("academic.local.upload.path")+documentDto.getDOC_FILE_PATH()));
        try{
            String content = "";
            Field[] fields = documentDto.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (!fields[i].getName().contains("DATA_HASH") && !fields[i].getName().contains("serialVersionUID")) {
                    content += fields[i].get(documentDto);
                }
            }
            documentDao.saveElectronic(documentDto.getDOC_ID(),"2","",eSign);
            //FileUtils.writeStringToFile(new File("D:\\tools\\ideaIU-2022.2.3\\IntelliJ IDEA 2022.2.3\\workspace\\shyhproject\\academic\\logs\\"+documentDto.getDOC_ID()+".txt"),content);
            documentDto.setDATA_HASH(Sha256.getSHA256Str(content));
            documentDao.saveObject(documentDto);
            acaLogsService.saveAcaLogs("Upload",String.format("<p>Document Name：%s</p><p>Upload Time：%s</p><p>Document Version：%s</p><p>Document Size：%s</p><p>Document Hash：%s</p>",documentDto.getDOC_TITLE(), documentDto.getDOC_UPLOAD_TIME(),documentDto.getDOC_VERSION(),documentDto.getDOC_SIZE(),documentDto.getDATA_HASH()),documentDto.getACA_ID());
            //添加至转换队列
            //ThreadUtil.execute(() -> kkfileViewService.addReviewConvertTask(documentDto.getDOC_ID(),documentDto.getDOC_FILE_REALNAME()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void viewByKey(String key,String eSign){
        try{
            documentDao.saveElectronic(key,"3","",eSign);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteDocument(String docId) {
        documentDao.deleteObject(docId);
    }

    public List<DocumentDto> getDocumentByOrg(String docOriginal) {
        List<DocumentDto> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(docOriginal) && !"undefined".equals(docOriginal)){
            String[] split = docOriginal.split(",");
            for (String s : split){
                list.add(documentDao.getObject(s));
            }
        }
        return list;
    }

    public String getEsign(String foreign){
        List<Map<String,Object>> l = documentDao.getMapList("SELECT * FROM CAS_BMDMS_ELECTRONIC_TAB WHERE FOREIGN_ID = '"+foreign+"'");
        return null == l || l.size() == 0?"0":"1";
    }

    public void checkIsChange(JdbcTemplate jdbc,String aca,String id){
        try{
            List<Map<String,Object>> data = null;
            try {
                data = jdbc.queryForList("SELECT * FROM CAS_BMDMS_DOCUMENT_TAB WHERE ACA_ID = '"+aca+"'");
            } catch (Exception e) {
                e.printStackTrace();
            }
            DocumentDto documentDto;
            String remark = "";
            for (int i = 0,j = data.size(); i < j; i++) {
                documentDto = JSONUtil.toBean(JSONUtil.parse(data.get(i)), DocumentDto.class, false);
                String content = "";
                Field[] fields = documentDto.getClass().getDeclaredFields();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    if (!fields[k].getName().contains("DATA_HASH") && !fields[k].getName().contains("serialVersionUID")){
                        content += fields[k].get(documentDto);
                    }
                }
                if (!StringUtils.equals(documentDto.getDATA_HASH(),Sha256.getSHA256Str(content))){//表示数据异常，存在被篡改的可能
                    remark += documentDto.getDOC_ID()+",";
                }
            }
            if (StringUtils.isNotEmpty(remark)){
                documentDao.excute("UPDATE IDGAR_DBS_TAB SET DB_STATUS = '1',db_remark='" + remark + "' WHERE DB_ID = '" + id + "'");
            }
        }catch (Exception e){
            Console.log(e.getMessage());
        }
    }

    public JSONArray isFalsify(String aca){
        List<Map<String,Object>> datas = documentDao.getMapList("SELECT B.USER_NAME USER_ID,A.ACA_ID,A.DB_REMARK FROM IDGAR_DBS_TAB A,IDGAR_USER_TAB B WHERE A.ACA_ID = '"+aca+"' AND A.DB_STATUS = '1' AND A.USER_ID = B.USER_ID");
        if (null != datas || datas.size() > 0){
            return JSONUtil.parseArray(datas);
        }
        return new JSONArray();
    }

}
