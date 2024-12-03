package com.core.extend.attachment.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.extend.attachment.dao.AdjunctDao;
import com.core.extend.attachment.dto.Adjunct;



/**
 * 附件操作service
 * @author ldl
 * 
 */

@Service("ADJ")
public class AdjunctService {

	@Autowired 
	AdjunctDao adjunctDao;

	/**添加附件
	 * auth:fanming
	 * @return
	 */
	public void saveAdjuct(Adjunct a){
		this.adjunctDao.saveObject(a);
	}

	public List<Adjunct > getDAdjunctList(String id){
		List<Adjunct> l = adjunctDao.getDAdjunctList(getAdjId(id));
		return l;
	}


	/**根据OTHER_ID获取对象集合
	 * auth:linxiaodong
	 * @return
	 */
	public List<Adjunct > getAdjunctList(String otherId){
		List<Adjunct> l = adjunctDao.getListByOtherid(otherId);
		return l;
	}

	public String getAdjId(String id){
		String []ids = id.split(",");
		id = "";
		for (String string : ids) {
			id +="'" + string + "',";
		}
		return id.substring(0,id.length()-1);
	}

	/**
	 * 保存附件信息到数据库
	 * @param listMaps 上传附件集合
	 * @param otherId 附件对应的关联外键
	 * @return 保存结果
	 * @author dongliangl
	 */
	public boolean saveAdjunct(final List<Map<String, Object>> listMaps,String otherId,final String path) {
		try {
			final Adjunct adjunct = new Adjunct();
			adjunct.setOTHERID(otherId);
			adjunct.setCREATETIME(new java.util.Date());
			for (int i = 0,j= listMaps.size(); i < j; i++) {
				adjunct.setADJDOWNURL(listMaps.get(i).get("path").toString());
				adjunct.setADJTRUENAME(listMaps.get(i).get("uuidName").toString());
				adjunct.setADJSHOWNAME(listMaps.get(i).get("realFileName").toString());
				adjunct.setADJSIZE(Integer.parseInt(listMaps.get(i).get("size").toString()));
				adjunct.setADJTYPE(listMaps.get(i).get("fileType").toString());
				//adjunct.setADJID(UUID.randomUUID().toString());
				adjunctDao.saveObjectByAutoUUID(adjunct);
				//saveByAutoUUID(adjunct);
			}
			return true;
		} catch (Exception e) {
			System.out.println("上传附件保存出错：");
			e.printStackTrace();
			return false;
		}

	} 
	public boolean saveAdjunct(List<Map<String, String>> listMaps,String otherId) {
		try {
			Adjunct adjunct = new Adjunct();
			adjunct.setOTHERID(otherId);
			adjunct.setCREATETIME(new java.util.Date());
			for (int i = 0,j= listMaps.size(); i < j; i++) {
				adjunct.setADJDOWNURL(listMaps.get(i).get("path").toString());
				adjunct.setADJTRUENAME(listMaps.get(i).get("uuidName").toString());
				adjunct.setADJSHOWNAME(listMaps.get(i).get("realFileName").toString());
				adjunct.setADJSIZE(Integer.parseInt(listMaps.get(i).get("size").toString()));
				adjunct.setADJTYPE(listMaps.get(i).get("fileType").toString());
				adjunctDao.saveObjectByAutoUUID(adjunct);
			}
			return true;
		} catch (Exception e) {
			System.out.println("上传附件保存出错：");
			e.printStackTrace();
			return false;
		}
		
	} 


	/**
	 * 根据文件名删除文件（库） 
	 * @param fileName  文件名
	 * @return   响应结果
	 */
	public boolean delAdjByFilename(String fileName) {
		try {
			adjunctDao.excute("DELETE FROM SYS_ADJUNCT WHERE ADJTRUENAME = ?", fileName.trim());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据附件所关联表删除附件
	 * @param otherid 附件表关联其他表id
	 * @param path 文件所在目录
	 * @return 删除响应
	 * by  ldl
	 */
	public boolean delAdjByOtherid(String otherid,String path){
		if (otherid==null||path==null) { 
			return true;
		}
		//此删除需要删除本地文件
		try {
			List<Adjunct> list = this.getAdjunctList(otherid);
			File file = null;
			for (int i = 0,j=list.size(); i < j; i++) {//循环删除本地文件  文件名需要未uuid格式
				Adjunct adjunct = list.get(i);
				if (adjunct!=null&&adjunct.getADJTRUENAME()!=null) {
					file = new File(path+adjunct.getADJTRUENAME());
					file.deleteOnExit();
				}
			}
			adjunctDao.deleteObjectBySql("DELETE FROM SYS_ADJUNCT WHERE OTHERID = ?", otherid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 返回附件集合
	 * @param comid 附件关联ID
	 * @author liuzd
	 * @return
	 */
	public List<Adjunct> getListByOtherid(String comid) {
		return adjunctDao.getListByOtherid(comid);
	}

	@Transactional
	public void deleteAllByOtherIds(String eventKey) {
		adjunctDao.deleteAllByOtherIds(eventKey);
	}

	public List<Adjunct> getObjectByOtherId(String hidtroubleid) {
		return adjunctDao.getObjectByOtherId(hidtroubleid);
	}


	public Adjunct getAdjByReanname(String name) {

		return adjunctDao.getObject("SELECT * FROM SYS_ADJUNCT WHERE ADJTRUENAME = ?", name.trim());

	}

	public boolean isOffice(String path){
		if (path.indexOf(".")<0) {
			return false;
		}
		String rex = path.substring(path.indexOf(".")).toLowerCase();
		if (rex.equals(".xls")||rex.equals(".xlsx")||rex.equals(".doc")||rex.equals(".docx")||rex.equals(".pdf")) {
			return true;
		}
		return false;
	}

	public String tranferSwf(Adjunct adjunct) {
		String downPath = adjunct.getADJDOWNURL();
		downPath = downPath.substring(0,downPath.indexOf("."));
		downPath = downPath + ".swf";
		try {
			adjunctDao.excute("UPDATE SYS_ADJUNCT SET FLASHURL = '"+downPath+"' WHERE ADJID = '"+adjunct.getADJID()+"'");
			return downPath;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
