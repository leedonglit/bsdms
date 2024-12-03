package com.isec.base.monit.service;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.StaticLog;
import com.core.security.page.AutoPage;
import com.core.tools.AppUserTool;
import com.core.tools.AppUserTool.AppUser;
import com.core.util.JavaMailUntil;
import com.core.util.Sha256;
import com.isec.base.monit.dao.UserDao;
import com.isec.base.monit.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("USER")
public class UserService {

	@Autowired
	UserDao userDao;

	public int checkLogin(String userId,String pwd,String ip) {
		UserDto user = userDao.getUser(userId, pwd);
		if(user == null) {
			return 3;
		}
		if (user != null && StringUtils.equals(user.getUser_status(), "1")) {
			AppUser appUser = new AppUser();
			appUser.setUserID(user.getUser_id());
			appUser.setUserName(user.getUser_name());
			AppUserTool.setAppUser(appUser);
			uptLastLogin(userId,ip,"/api/login");
			return Integer.parseInt(user.getUser_status());
		}else {
			return Integer.parseInt(user.getUser_status());
		}
	}

	public AppUser getLoginUser() {
		return AppUserTool.getAppUser();
	}

	public AutoPage<UserDto> loadAllUser(AutoPage<UserDto> page) {
//		page.setResult(userDao.getAllList());
//		page.setTotalCount(page.getResult().size());
//		page.setTotalpage(1);
		page.addCondition("user_isdel", "=", "0");
		return userDao.findAutoPage(page);
	}

	public UserDto loadUser() {
		String userId = AppUserTool.getAppUser().getUserID();
		UserDto u = userDao.getObject(userId);
		//u.setUser_pwd("");
		return u;
	}

//	public void upgradeLevel() {
//		String userId = AppUserTool.getAppUser().getUserID();
//		userDao.excute("UPDATE IDGAR_USER_TAB SET USER_LEVEL = 2 WHERE USER_ID = ?",userId);
//	}

	public void saveNode(String db,String u, String p, String url,String aca) {
		String userId = AppUserTool.getAppUser().getUserID();
		userDao.excute("INSERT INTO IDGAR_DBS_TAB(DB_ID,DB_NAME,DB_URL,DB_USER,DB_PWD,DB_DRIVER,USER_ID,ACA_ID,DB_STATUS,UP_TIME) VALUE('"+IdUtil.fastSimpleUUID()+"','" + db + "','"+url+"','"+u+"','"+p+"','com.mysql.jdbc.Driver','"+userId+"','"+aca+"','0',NOW())");
	}

	public void saveUser(UserDto userDto) {
		userDao.saveObject(userDto);
	}

	public boolean register(UserDto userDto) {
		try {
			userDto.setUser_regtime(new Date());
			userDto.setUser_status("0");
			userDto.setUser_from("0");
			userDto.setUser_level(1);
			userDto.setUser_isdel("0");
			userDto.setUser_nameimg("");
			userDto.setUser_hash(Sha256.getSHA256Str(getUserHash(userDto)));
			userDao.saveObject(userDto);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 过期时间60s * 1000
	 */
	Cache<String, String> emailCodeCache = CacheUtil.newTimedCache(60000);

	public boolean sendEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return true;
		}
		try {
			String code = RandomUtil.randomNumbers(4)+"";
			emailCodeCache.put(email, code);
			JavaMailUntil.sendEmail(email, "<font style='font-size:30px;font-weight:bold'>"+code+"</font><br/><br/>This is your email verification code. Please do not tell anyone, it is valid for one minute.", "IDGAR email verification code");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean emailIsExist(String email) {
		Integer count = userDao.getInt("SELECT COUNT(*) FROM IDGAR_USER_TAB WHERE USER_EMAIL = ? ", email);
		return count == 0 ? false : true;
	}

	public boolean checkEmailCode(String code,String email) {
		if (emailCodeCache.containsKey(email) && emailCodeCache.get(email).equals(code)) {
			return true;
		}
		return false;
	}

	public boolean resetPwd(String pwd,String email) {
		try {
			userDao.excute("UPDATE IDGAR_USER_TAB SET USER_PWD = ? WHERE USER_EMAIL = ?",pwd,email);
			emailCodeCache.remove(email);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//1上传数据合约2下载数据合约
	public void saveHy(String content,String user,String date,String nImg,String type) {
		userDao.excute("insert into igdar_hy_tab(hy_id,hy_content,hy_user,hy_nimg,hy_date,hy_type,op_time) value('"+UUID.fastUUID().toString()+"','"+content+"','"+user+"','"+nImg+"','"+date+"','"+type+"',NOW())");
	}

	/**
	 * 更改用户状态
	 * @param userId
	 * @param status 状态0锁定1激活
	 * @return 
	 */
	public boolean activeUser(String userId,String status) {
		try {
			userDao.excute("UPDATE IDGAR_USER_TAB SET USER_STATUS = ? WHERE USER_ID = ?",status,userId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void uptLastLogin(String userId,String from,String path) {
		userDao.excute("INSERT INTO IDGAR_USER_LOGS_TAB(USER_ID,LOG_CONENT,LOG_STATUS,LOG_TIME,LOG_FROM,LOG_TYPE,REQ_PATH) VALUE(?,?,?,NOW(),?,?,?)"
				,userId,"login","0",from,"4",path);
	}

	public List<Map<String, Object>> loadUserFiles() {
		String userId = AppUserTool.getAppUser().getUserID();
		return this.userDao.getMapList("SELECT * FROM IDGAR_USER_FILES_TAB WHERE USER_ID  = (SELECT USER_EMAIL FROM IDGAR_USER_TAB WHERE USER_ID = '"+userId+"')" );
	}

	public Map<String, Object> loadUserFile(String file_id) {
		List<Map<String, Object>> l = this.userDao.getMapList("SELECT * FROM IDGAR_USER_FILES_TAB WHERE FILE_ID  = '"+file_id+"'" );
		return null != l?l.get(0):null;
	}

	public List<Map<String, Object>> loadAllFiles() {
		List<Map<String, Object>> fs = this.userDao.getMapList("SELECT * FROM IDGAR_USER_FILES_TAB" );
		return fs;
	}

	private String getUserHash(UserDto userDto) {
		String params = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",				
				userDto.getUser_id() == null ?"":userDto.getUser_id(), 
				userDto.getUser_no() == null ?"":userDto.getUser_no(), 
				userDto.getUser_name() == null ?"":userDto.getUser_name(), 
				userDto.getUser_pwd() == null ?"":userDto.getUser_pwd(), 
				userDto.getUser_status() == null ?"":userDto.getUser_status(), 
				userDto.getUser_ip() == null ?"":userDto.getUser_ip(), 
				userDto.getUser_from() == null ?"":userDto.getUser_from(), 
				userDto.getUser_regtime() == null ?"":userDto.getUser_regtime(), 
				userDto.getUser_email() == null ?"":userDto.getUser_email(), 
				userDto.getUser_interests() == null ?"":userDto.getUser_interests(), 
				userDto.getUser_org() == null ?"":userDto.getUser_org(), 
				userDto.getUser_page() == null ?"":userDto.getUser_page(), 
				userDto.getUser_nameimg() == null ?"":userDto.getUser_nameimg(), 
				userDto.getUser_isdel() == null ?"":userDto.getUser_isdel(), 
				userDto.getUser_level() == null ?"":userDto.getUser_level());
		return params;
	}

	public int loadUserFilesCount(String email) {
		int count = userDao.getInt("SELECT COUNT(*) FROM IDGAR_USER_FILES_TAB WHERE USER_ID= ? ", email);
		return count;
	}


	public UserDto getUserById(String userId){
		return userDao.getObject(userId);
	}

	public Integer getCounts(){
		int count = userDao.getInt("SELECT COUNT(*) FROM IDGAR_USER_TAB WHERE USER_ISDEL = 0 and USER_STATUS='0'");
		return count;
	}


	public void batchOperationUser(String ids, String type) {
		String[] split = ids.split(",");
		String str = "";
		for (String id : split){
			str += "'"+id+"',";
		}
		ids = str.substring(0,str.length()-1);
		switch (type){
			//锁定
			case "1":
				userDao.excute("UPDATE IDGAR_USER_TAB SET USER_STATUS = '0' WHERE USER_ID IN ("+ids+")");
				break;
			//激活
			case "2":
				userDao.excute("UPDATE IDGAR_USER_TAB SET USER_STATUS = '1' WHERE USER_ID IN ("+ids+")");
				break;
			//删除
			case "3":
				userDao.excute("UPDATE IDGAR_USER_TAB SET user_isdel = '1' WHERE USER_ID IN ("+ids+")");
				break;
		}
	}

	public boolean updateUser(UserDto selectUser) {
		try {
			UserDto userDto = userDao.updateObject(selectUser);
			if(userDto != null){
				return true;
			}
			return false;
		}catch (Exception e){
			StaticLog.info("updateUser Exception:"+e.getMessage());
			return false;
		}

	}
}
