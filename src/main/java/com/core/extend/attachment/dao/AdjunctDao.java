package com.core.extend.attachment.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.core.extend.attachment.dto.Adjunct;
import com.core.security.database.jdbc.BaseDao;

@Repository
public class AdjunctDao extends BaseDao<Adjunct,String> {

	public List<Adjunct> getListByOtherid(String comid) {
		String sql = "SELECT * FROM SYS_ADJUNCT WHERE OTHERID = ? ORDER BY CREATETIME DESC";
		return this.getList(sql, new Object[]{comid});
	}

	public List<Adjunct> getDAdjunctList(String id){
		String sql = "select * from SYS_ADJUNCT where OTHERID in("+id+")";
		return this.getList(sql, new Object[]{});
	}

	public void deleteAllByOtherIds(String eventKey) {
		String sql = "delete from SYS_ADJUNCT where OTHERID = '" + eventKey + "' ";
		this.excute(sql);
	}

	public List<Adjunct> getObjectByOtherId(String hidtroubleid) {
		String sql = "select t.* from SYS_ADJUNCT t where t.OTHERID = '" + hidtroubleid + "'";
		return this.getList(sql);
	}

	public void save(Adjunct adjunct) {
		this.save(adjunct);
	} 

}
