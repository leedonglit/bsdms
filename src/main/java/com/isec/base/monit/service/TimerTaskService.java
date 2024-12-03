package com.isec.base.monit.service;

import cn.hutool.core.thread.ThreadUtil;
import com.common.EncryptUtil;
import com.core.tools.DBTool;
import com.core.tools.FinalVarStaticTool;
import com.isec.base.monit.dao.DocumentDao;
import com.isec.util.KettleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

@EnableScheduling//开启定时任务
@Component
public class TimerTaskService {

    @Autowired
    Environment env;
    @Autowired
    DocumentDao documentDao;

    @Autowired
    DocumentService documentService;

    /**
     * 数据防篡改校验频率
     * @Author ldonglit
     * @Date 2024年3月20日20:40:06
     * @TODO 每五分钟校验
     */
    @Scheduled(cron="0 0 */1 * * ? ")//每1小时触发一次
    public void cron(){
        if (FinalVarStaticTool.ISUPLOAD_DATA) {
            return;
        }
        FinalVarStaticTool.ISUPLOAD_DATA = true;
        ThreadFactory threadPool = ThreadUtil.createThreadFactoryBuilder().build();
        List<Map<String, Object>> nodes = documentDao.getMapList("SELECT * FROM IDGAR_DBS_TAB WHERE DB_STATUS = '0'");
        for (int r = 0; r < nodes.size(); r++) {
            int finalR = r;
            threadPool.newThread(()->{
                List<Map<String, Object>> acas = documentDao.getMapList("SELECT * FROM IDGAR_DBS_TAB WHERE DB_STATUS = '0' AND ACA_ID = '" + nodes.get(finalR).get("ACA_ID") + "'");
                if (null == acas || acas.size() < 2){
                    return;
                }
                JdbcTemplate jdbc;
                List<String[]> flag = new ArrayList<>();
                for (int i = 0; i < acas.size(); i++) {
                    String id = acas.get(i).get("db_id").toString();
                    String un = acas.get(i).get("db_user").toString();
                    String up = acas.get(i).get("db_pwd").toString();
                    String url = acas.get(i).get("db_url").toString();
                    String aca = acas.get(i).get("ACA_ID").toString();
                    try {
                        if (DBTool.isConnect(un, up, url)) {
                            jdbc = DBTool.getJdbcTemplate(un,up,url,"mysql");
                            documentService.checkIsChange(jdbc,aca,id);
                        }
                    } catch (Exception e) {
                        System.out.println("数据库连接异常");
                    }
                }
            }).start();
        }
        FinalVarStaticTool.ISUPLOAD_DATA = false;
    }

    //每24小时进行节点数据异常数据恢复一次
    @Scheduled(cron="0 0 */2 * * ? ")
    public void two(){
        try {
            List<Map<String, Object>> nodes = documentDao.getMapList("SELECT DISTINCT ACA_ID FROM IDGAR_DBS_TAB");
            for (int r = 0; r < nodes.size(); r++) {
                int finalR = r;
                List<Map<String, Object>> acas = documentDao.getMapList("SELECT * FROM IDGAR_DBS_TAB WHERE DB_STATUS = '0' AND ACA_ID = '" + nodes.get(finalR).get("ACA_ID") + "'");
                if (null == acas || acas.size() < 2) {
                    return;
                }
                for (int i = 0; i < acas.size(); i++) {
                    String un = acas.get(i).get("db_user").toString();
                    String up = acas.get(i).get("db_pwd").toString();
                    String url = acas.get(i).get("db_url").toString();
                    JdbcTemplate jdbc = DBTool.getJdbcTemplate(un,up,url,"mysql");
                    //先把节点异常数据清除
                    jdbc.execute("DELETE FROM CAS_BMDMS_DOCUMENT_TAB WHERE ACA_ID = '" + nodes.get(finalR).get("ACA_ID") + "'");
                    //再同步其他节点正常数据过来
                    synData(nodes.get(finalR).get("ACA_ID")+"","jdbc:mysql://119.189.0.152:3306/academic?useUnicode=true&useSSL=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8","ldonglit1q!",url,up);
                }
            }
        } catch (Exception e) {
            System.out.println("数据库连接异常");
        }
    }

    //每24小时进行节点数据异常数据恢复一次
    @Scheduled(cron="0 0 */24 * * ? ")
    public void day(){
        try {
            List<Map<String, Object>> nodes = documentDao.getMapList("SELECT DISTINCT ACA_ID FROM IDGAR_DBS_TAB");
            for (int r = 0; r < nodes.size(); r++) {
                int finalR = r;
                List<Map<String, Object>> acas = documentDao.getMapList("SELECT * FROM IDGAR_DBS_TAB WHERE DB_STATUS = '1' AND ACA_ID = '" + nodes.get(finalR).get("ACA_ID") + "'");
                if (null == acas || acas.size() < 2) {
                    return;
                }
                for (int i = 0; i < acas.size(); i++) {
                    //修改该节点数据异常的状态为正常
                    documentDao.excute("UPDATE IDGAR_DBS_TAB SET DB_STATUS = '0' WHERE DB_ID = '" + acas.get(i).get("db_id").toString() + "'");
                }
            }
        } catch (Exception e) {
            System.out.println("数据库连接异常");
        }
    }

    public void synData(String aca,String ydb_url,String ydb_pwd,String tdb_url,String tdb_pwd) {
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        String ydb_ip = ydb_url.split(":")[2].replaceAll("//", "");
        String tdb_ip = tdb_url.split(":")[2].replaceAll("//", "");
        map.put("aca", aca);
        map.put("ydb_ip", ydb_ip);
        map.put("ydb_pwd", EncryptUtil.encryptPassword(ydb_pwd));
        map.put("tdb_ip", tdb_ip);
        map.put("tdb_pwd", EncryptUtil.encryptPassword(tdb_pwd));
        KettleUtil.runKettleTransfer(map, env.getProperty("academic.local.ktrPath"));
        System.out.println("transfer data time:" + ( System.currentTimeMillis() - start ));
    }
}
