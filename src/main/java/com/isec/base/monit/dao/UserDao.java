package com.isec.base.monit.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.core.security.database.jdbc.BaseDao;
import com.isec.base.monit.dto.UserDto;

@Repository
public class UserDao extends BaseDao<UserDto, String>{

	
	public UserDto getUser(String userId,String pwd) {

		return this.getObject("SELECT * FROM IDGAR_USER_TAB WHERE USER_EMAIL = ? AND USER_PWD = ?", userId,pwd);
	}
	
	
	public void activeUser(String userId,String user_status) {

		this.excute("UPDATE IDGAR_USER_TAB SET USER_STATUS = ? WHERE USER_ID = ?",user_status, userId);
	}
	
	

	public List<UserDto> getAllUser() {

		return this.getList("SELECT * FROM IDGAR_USER_TAB ORDER BY USER_REGTIME DESC");
	}
}
