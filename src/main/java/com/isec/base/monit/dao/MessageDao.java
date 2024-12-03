package com.isec.base.monit.dao;

import com.core.security.database.jdbc.BaseDao;
import com.isec.base.monit.dto.MessageDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao extends BaseDao<MessageDto,String> {


    public List<MessageDto> loadMessageByUser(String user) {
        return getList("SELECT * FROM CAS_BMDMS_MESSAGE_TAB WHERE MSG_RECEIVER = ? AND MSG_ISDEL = 0 ORDER BY MSG_TIME DESC LIMIT 4", user);
    }
}
