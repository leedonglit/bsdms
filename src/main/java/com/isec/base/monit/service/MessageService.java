package com.isec.base.monit.service;

import cn.hutool.core.util.IdUtil;
import com.core.security.page.AutoPage;
import com.core.security.page.ColDefine;
import com.core.tools.AppUserTool;
import com.isec.base.monit.dao.MessageDao;
import com.isec.base.monit.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;


    public void saveMessage(String title,String content,String receiver,String foreign){
        try{
            MessageDto messageDto = new MessageDto();
            messageDto.setMSG_TIME(new Date());
            messageDto.setMSG_ISDEL(0);
            messageDto.setMSG_ID(IdUtil.fastSimpleUUID());
            messageDto.setMSG_CONTENT(content);
            messageDto.setMSG_TITLE(title);
            messageDto.setMSG_FOREIGN(foreign);
            messageDto.setMSG_RECEIVER(receiver);
            messageDto.setMSG_SENDER(AppUserTool.getAppUser().getUserID());
            messageDao.saveObject(messageDto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public List<MessageDto> loadMessageByUser(){
        if (AppUserTool.isLogin()){
            return messageDao.loadMessageByUser(AppUserTool.getAppUser().getUserID());
        }else {
            return null;
        }
    }

    public MessageDto loadMessageById(String id){
        return messageDao.getObject(id);
    }

    public AutoPage<MessageDto> loadAllMsg(AutoPage<MessageDto> page){
        String sql = "SELECT * FROM CAS_BMDMS_MESSAGE_TAB WHERE";
        sql += " (MSG_RECEIVER = '"+AppUserTool.getAppUser().getUserID()+"' or MSG_SENDER = '"+AppUserTool.getAppUser().getUserID()+"') AND MSG_ISDEL = '0'";
        ColDefine[] condition = page.getCondition();
        for (int i = 0; i < condition.length; i++){
            ColDefine colDefine = condition[i];
            sql += " and "+colDefine.getCol()+" "+colDefine.getType()+" '"+colDefine.getValue()[0]+"'";
        }
        return messageDao.findAutoPageBySQL(page,sql);
    }


    public Integer getCounts(){
        String user = AppUserTool.getAppUser().getUserID();
        int count = messageDao.getInt("SELECT COUNT(*) FROM CAS_BMDMS_MESSAGE_TAB WHERE (MSG_RECEIVER = ? OR MSG_SENDER = ?) AND MSG_ISDEL = 0",user,user);
        return count;
    }


    public void deleteBatch(String ids) {
        String[] split = ids.split(",");
        for (String id : split){
            String sql = "update CAS_BMDMS_MESSAGE_TAB set MSG_ISDEL = '1' where MSG_ID = '"+id+"'";
            messageDao.excute(sql);
        }
    }
}
