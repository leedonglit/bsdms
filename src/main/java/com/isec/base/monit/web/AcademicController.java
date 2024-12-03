package com.isec.base.monit.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.common.FileUtil;
import com.core.security.page.AutoPage;
import com.core.security.page.JsonResult;
import com.core.security.page.PageSupport;
import com.core.tools.AppUserTool;
import com.core.tools.DBTool;
import com.isec.base.monit.dto.AcademicDto;
import com.isec.base.monit.dto.DocumentDto;
import com.isec.base.monit.dto.GroupDto;
import com.isec.base.monit.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
@RequestMapping("/academic")
public class AcademicController {

    @Autowired
    Environment env;

    @Autowired
    AcademicService academicService;

    @RequestMapping("/projectdeleteBatch")
    @ResponseBody
    public JsonResult projectdeleteBatch(String ids){
        academicService.projectdeleteBatch(ids);
        return new JsonResult("Delete Success");
    }

    @RequestMapping("/deletePhase")
    @ResponseBody
    public JsonResult deletePhase(String phaseId){
        academicService.deletePhase(phaseId);
        return new JsonResult("Delete Success");
    }

    @RequestMapping("/deleteDocument")
    @ResponseBody
    public JsonResult deleteDocument(String docId){
        documentService.deleteDocument(docId);
        return new JsonResult("Delete Success");
    }

    @RequestMapping("/preview")
    public ResponseEntity<FileSystemResource> preview(String fileId, String fullfilename){
        DocumentDto documentBydocId = documentService.getDocumentBydocId(fileId);
        /**
         * 1.解密文件
         * File file = new File(env.getProperty("academic.local.upload.path") + File.separator + documentBydocId.getDOC_FILE_PATH());
         */
        File file = FileUtil.decryptFile(env.getProperty("academic.local.upload.path") + File.separator + documentBydocId.getDOC_FILE_PATH());
        FileSystemResource fileResource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fullfilename)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileResource);
    }

    @RequestMapping("/loadAll")
    @ResponseBody
    public JsonResult loadAcademic(@RequestParam String jsonpage){
        AutoPage<AcademicDto> page = PageSupport.fromJsonAutoPage(jsonpage);
        page = academicService.loadAcademic(page);
        PageSupport.loadGridHtml("/module/base/academic/include/list", page);
        return new JsonResult(page);
    }

    @RequestMapping("/loadParticipated")
    @ResponseBody
    public JsonResult loadParticipated(@RequestParam String jsonpage){
        AutoPage<AcademicDto> page = PageSupport.fromJsonAutoPage(jsonpage);
        page = academicService.loadParticipated(page);
        PageSupport.loadGridHtml("/module/base/participated/include/list", page);
        return new JsonResult(page);
    }

    @RequestMapping("/toAdd")
    public ModelAndView toAdd(HttpServletRequest request) {
        String acaId = request.getParameter("acaId");
        ModelAndView model = new ModelAndView();
        if (StringUtils.isEmpty(acaId)){
            model.addObject("operate","add");
            model.addObject("pro_code",IdUtil.getSnowflakeNextIdStr());
            model.addObject("pro_start_date",LocalDate.now());
        }else{
            AcademicDto academicDto = academicService.getAcademicById(acaId);
            model.addObject("academic",academicDto);
            model.addObject("phase",academicService.getPhaseByAca(acaId));
            List<DocumentDto> documentByAcas = documentService.getDocumentByAca(academicDto.getACA_ID());
            model.addObject("documents",documentByAcas);
            model.addObject("operate","edit");
        }
        model.addObject("acaId",acaId);
        model.setViewName("/module/base/academic/addPage");
        return model;
    }

    @RequestMapping("/createProject")
    @ResponseBody
    public JsonResult createProject(@RequestParam String phase,String pro){
        academicService.createProject(pro,phase);
        return new JsonResult(true);
    }

    @RequestMapping("/openEsign")
    public ModelAndView openEsign(){
        ModelAndView model = new ModelAndView("/module/base/academic/include/esign");
        model.addObject("currDate", LocalDate.now());
        return model;
    }

    @RequestMapping("/uptoGradePage")
    public ModelAndView uptoGradePage(){
        ModelAndView model = new ModelAndView("/module/base/academic/include/upgrade");
        return model;
    }

    @Autowired
    TimerTaskService timerTaskService;

    @RequestMapping("/initNode")
    @ResponseBody
    public JsonResult initNode(HttpServletRequest request) {
        String u = request.getParameter("u");
        String p = request.getParameter("p");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String db = request.getParameter("db");
        String aca = request.getParameter("aca");
        String url = "jdbc:mysql://"+ip+":"+port+"/"+db+"?useSSL=false&characterEncoding=utf-8&autoReconnect=true";
        JsonResult j = new JsonResult();
        j.setData(false);
        try {
            if(DBTool.isConnect(u, p, url)) {
                DBTool.runScript(u, p, url,env.getProperty("academic.local.sql.path"));
                userService.saveNode(db,u, p, url,aca);
                timerTaskService.synData(aca,"jdbc:mysql://119.189.0.152:3306/academic?useUnicode=true&useSSL=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8","ldonglit1q!",url,p);
                j.setData(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return j;
    }

    @RequestMapping("/submitDocument")
    @ResponseBody
    public JsonResult submitDocument(DocumentDto documentDto,String eSign){
        documentService.submitDocument(documentDto,eSign);
        return new JsonResult(true);
    }

    @RequestMapping("/viewByKey")
    @ResponseBody
    public JsonResult viewByKey(String key,String eSign){
        documentService.viewByKey(key,eSign);
        return new JsonResult(true);
    }

    @RequestMapping("/openDown/{aca}/{ids}")
    public ModelAndView openDown(@PathVariable("aca") String aca,@PathVariable("ids") String ids) {
        ModelAndView model = new ModelAndView();
        model.addObject("docs",documentService.getDocumentByAca(aca,ids));
        model.addObject("password", RandomUtil.randomString(8));
        model.addObject("docIds",ids);
        model.addObject("username",AppUserTool.isLogin()?AppUserTool.getAppUser().getUserName():"");
        model.addObject("currDate", LocalDate.now());
        model.setViewName("/module/base/academic/include/download");
        return model;
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response,HttpServletRequest request){
        String key = request.getParameter("key");
        String password = request.getParameter("password");
        String eContact = request.getParameter("eContact");
        String eSign = request.getParameter("eSign");
        String docIds = request.getParameter("docIds");
//        if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(eContact)){
        if(StringUtils.isNotEmpty(key)){
            academicService.saveDown(response,password,eContact,eSign,key,docIds);
        }
    }

    @RequestMapping("/re-download/{id}")
    public void reDownload(HttpServletResponse response,@PathVariable("id") String id){
        academicService.saveDown(response,id);
    }

    @RequestMapping("/openHistory/{key}/{aca}")
    public ModelAndView openHistory(@PathVariable("key") String key,@PathVariable("aca") String aca) {
        ModelAndView model = new ModelAndView();
        JSONArray downloadHistory = academicService.getDownloadHistory(key, aca);
        if(downloadHistory.size()>0){
            model.addObject("histories",downloadHistory);
            model.setViewName("/module/base/academic/include/history");
        }else{
            model.setViewName("/module/base/academic/include/nohistory");
        }

        return model;
    }

    @RequestMapping("/openUpload")
    public ModelAndView openUpload() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/module/base/academic/include/upload");
        return model;
    }

    @Autowired
    DocumentService documentService;

    @Autowired
    AcaLogsService acaLogsService;

    @Autowired
    GroupService groupService;

    @Autowired
    KkfileViewService kkfileViewService;

    /**
     * home页搜索功能
     * @return
     */
    @RequestMapping("/content")
    public ModelAndView toContent(HttpServletRequest request) {
        String key = request.getParameter("key");
        String acaId = request.getParameter("acaId");
        ModelAndView model = new ModelAndView();
        model.addObject("kkfileurl",env.getProperty("kkfileview.ip")+":"+env.getProperty("kkfileview.prot"));
        model.addObject("serverurl",env.getProperty("local.openIp")+":"+env.getProperty("server.port"));
        if (StringUtils.isNotEmpty(key)){//表示通过key访问项目内容
            JSONObject keyParam = academicService.getKeyById(key);
            if (null != keyParam && new Date().getTime()/1000 < keyParam.getInt("failureTime")){
                AcademicDto academicDto = academicService.getAcademicById(keyParam.getStr("ACA_ID"));
                List<DocumentDto> documentByAcas = documentService.getDocumentByAca(academicDto.getACA_ID());
                if (documentByAcas != null && !documentByAcas.isEmpty()) {
                    documentByAcas.forEach((doc)->{
                        doc.setDOC_USER(userService.getUserById(doc.getDOC_USER()).getUser_name());
                    });
                }
                model.addObject("academic",academicDto);
                model.addObject("documents",documentByAcas);
                model.addObject("logs",acaLogsService.getLogsByAca(academicDto.getACA_ID()));
                model.addObject("groups",groupService.getGroupsByAca(academicDto.getACA_ID()));
                model.addObject("role",-1);
                model.addObject("key",key);
                model.addObject("isUpgrade","0");
                model.addObject("haveEsign",documentService.getEsign(key));
                model.addObject("data",makeData(documentByAcas));
                model.setViewName("/module/base/detail/content");
                return model;
            }
        } else if (StringUtils.isNotBlank(acaId)) {//表示通过项目ID访问项目内容，项目组内成员打开
            if (AppUserTool.isLogin()){
                GroupDto groupDto = groupService.getGroupByAca(AppUserTool.getAppUser().getUserID(),acaId);
                if (null != groupDto){
                    AcademicDto academicDto = academicService.getAcademicById(acaId);
                    List<DocumentDto> documentByAcas = documentService.getDocumentByAca(acaId);
                    if (documentByAcas != null && !documentByAcas.isEmpty()) {
                        documentByAcas.forEach((doc)->{
                            doc.setDOC_USER(userService.getUserById(doc.getDOC_USER()).getUser_name());
                        });
                    }
                    model.addObject("academic",academicDto);
                    model.addObject("documents",documentByAcas);
                    model.addObject("logs",acaLogsService.getLogsByAca(acaId));
                    model.addObject("groups",groupService.getGroupsByAca(acaId));
                    model.addObject("role",groupDto.getGP_ROLE());
                    model.addObject("data",makeData(documentByAcas));
                    model.addObject("isUpgrade",documentService.checkIsNodeUser(acaId));
                    model.addObject("falsify",documentService.isFalsify(acaId).toString());
                    model.setViewName("/module/base/detail/content");
                    return model;
                }
            }
        }
        model.setViewName("/module/base/detail/noPermission");
        return model;
    }

    @Autowired
    UserService userService;

    private String makeData(List<DocumentDto> dbacas) {
        JSONObject data = JSONUtil.createObj();
        JSONArray nodes = JSONUtil.createArray();
        JSONArray links = JSONUtil.createArray();
        try {
            List<DocumentDto> documentByAcas = null;
            if(dbacas != null && dbacas.size() > 0){
                documentByAcas = new ArrayList<>();
                for (int i = 0; i < dbacas.size(); i++) {
                    documentByAcas.add(new DocumentDto());
                }
                if (documentByAcas.size() == dbacas.size()) {
                    Collections.copy(documentByAcas, dbacas);
                }
            }
            if(documentByAcas != null && documentByAcas.size() > 0){
                createNode(nodes, documentByAcas);
                creatLink(links, documentByAcas);
                data.set("nodes",nodes);
                data.set("links",links);
            }else{
                return data.toString();
            }
        }catch (Exception e){
            StaticLog.info("makeData error");
        }
        return data.toString();
    }

    private void creatLink(JSONArray links, List<DocumentDto> documentByAcas) {
        if(documentByAcas != null && documentByAcas.size() > 0){
            for (int i = 0; i < documentByAcas.size(); i++){
                DocumentDto documentDto = documentByAcas.get(i);
                if(StringUtils.isNotEmpty(documentDto.getDOC_ORIGINAL())){
                    String[] split = documentDto.getDOC_ORIGINAL().split(",");
                    for (String s : split){
//                        DocumentDto orgDocumentDto =documentService.getDocumentBydocId(s);
                        JSONObject link = JSONUtil.createObj();
                        link.set("source",documentDto.getDOC_ID());
                        link.set("target",s);
                        links.add(link);
                    }
//                    List<DocumentDto> documentByOrgs = documentService.getDocumentByOrg(documentDto.getDOC_ORIGINAL());
//                    creatLink(links, documentByOrgs);
                }
            }
        }
    }

    private void createNode(JSONArray nodes, List<DocumentDto> documentByAcas) {
        if(documentByAcas != null && documentByAcas.size() > 0){
            for (int i = 0; i < documentByAcas.size(); i++){
                DocumentDto documentDto = documentByAcas.get(i);
                JSONObject node = JSONUtil.createObj();
                node.set("id",documentDto.getDOC_ID());
                node.set("name",documentDto.getDOC_TITLE());
                node.set("symbol","image://"+env.getProperty("academic.local.baseUrl")+"/module/base/images/file.png");
//                node.set("x",Math.random() * (1000 - 50) + 25);
//                node.set("y",Math.random() * (1000 - 50) + 2);
                JSONObject childproe= JSONUtil.createObj();
                childproe.set("user",documentDto.getDOC_USER());
                childproe.set("date",documentDto.getDOC_UPLOAD_TIME());
                childproe.set("hash",documentDto.getDATA_HASH());
                node.set("properties",childproe);
                nodes.add(node);
            }
        }
    }

    @RequestMapping("/upload/{aca}")
    @ResponseBody
    public JsonResult upload(@RequestParam("file") MultipartFile[] files,@PathVariable("aca") String aca){
        return new JsonResult(academicService.upload(files,aca));
    }

    @RequestMapping("/rmGroup")
    @ResponseBody
    public JsonResult removeGroup(@RequestParam String group){
        return new JsonResult(groupService.removeGroup(group));
    }

    @RequestMapping("/beManager")
    @ResponseBody
    public JsonResult beManager(@RequestParam String group){
        return new JsonResult(groupService.beManager(group));
    }

    @RequestMapping("/toGroup")
    @ResponseBody
    public JsonResult toGroup(@RequestParam Integer t,String acaId){
        return  new JsonResult(academicService.createLink(acaId,IdUtil.fastSimpleUUID(),t));
    }

    @RequestMapping("/saveKey")
    @ResponseBody
    public JsonResult saveKey(@RequestParam Integer failureTime,String acaId,String key,String desc_content,String docIds){
        return  new JsonResult(academicService.saveKey(acaId,key,failureTime,desc_content,docIds));
    }

    @RequestMapping("/delKey")
    @ResponseBody
    public JsonResult delKey(@RequestParam String key){
        return  new JsonResult(academicService.delKey(key));
    }

    @RequestMapping("/loadKey/{acaId}")
    @ResponseBody
    public JsonResult loadKey(@PathVariable("acaId") String acaId){
        Map<String,Object> map = new HashMap<>();
        map.put("result",academicService.loadKey(acaId));
        map.put("acaId",acaId);
        String html = PageSupport.loadObjHtml("/module/base/account/list/keys", map);
        return new JsonResult(html);
    }

    @RequestMapping("/addGroup")
    public ModelAndView addGroup(@RequestParam String param){
        ModelAndView model = new ModelAndView();
        if (groupService.addGroup(param)){
            model.addObject("title","Join successfully!");
            model.addObject("content","Congratulations on successfully joining the project team!");
        } else {
            model.addObject("title","Join failed!");
            model.addObject("content","Sorry, joining the project team failed. You may not have permission, or the link may have expired!");
        }
        model.setViewName("/module/base/academic/include/noPermission");
        return model;
    }

    @RequestMapping("/openKeyPage")
    public ModelAndView openKeyPage(@RequestParam String acaId) {
        ModelAndView model = new ModelAndView();
        model.addObject("KEY",IdUtil.fastSimpleUUID().toUpperCase());
        model.addObject("acaId",acaId);
        List<DocumentDto> documentByAcas = documentService.getDocumentByAca(acaId);
        model.addObject("documents",documentByAcas);
        model.setViewName("/module/base/account/include/keyPage");
        return model;
    }

}
