package com.isec.base.monit.service;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.StaticLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.validation.executable.ValidateOnExecution;

/**
 * kkFileView为文件文档在线预览解决方案，
 * 该项目使用流行的spring boot搭建，易上手和部署，
 * 基本支持主流办公文档的在线预览，
 * 如doc,docx,xls,xlsx,ppt,pptx,pdf,txt,zip,rar,图片,视频,音频等等
 * @Author zhangxuhui
 * @Date 2024/9/9
 * @email zxh_1633@163.com
 */
@Service
public class KkfileViewService {

    @Autowired
    Environment env;

    //kkFileView 服务ip
    @Value("${kkfileview.ip}")
    private String kkip;

    //kkFileView 服务端口
    @Value("${kkfileview.prot}")
    private String kkprot;

    @Value("${local.openIp}")
    private String openIp;

    /**
     * 添加文件转换任务
     * 将需要预览的文件url放入队列中，提前进行转码
     * var url = 'http://127.0.0.1:8080/file/test.txt'
     * http://127.0.0.1:8012/addTask?url=http://xxx/test.txt
     * var previewUrl = "http://"+serverUrl+"/academic/preview?fileId="+docId+"&fullfilename="+fileName;
     * @param docId fileName
     */
    public void addReviewConvertTask(String docId,String fileName){
        try {
            String url = "http://"+ openIp +":"+env.getProperty("server.port")+"/academic/preview?fileId="+docId+"&fullfilename="+fileName;
            StaticLog.info("kkFileView 文件提前转换，url："+ url);
            String kkfileviewUrl = "http://"+kkip+":"+kkprot+"/addTask?url="+url;
            String s = HttpUtil.get(kkfileviewUrl);
            StaticLog.info("kkFileView 文件提前转换结果："+ s);
        }catch (Exception e){
            StaticLog.info("kkFileView 文件提前转换，Exception："+ e.getMessage());
        }
    }
}
