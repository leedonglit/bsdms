package com.isec.util;

import java.util.Iterator;
import java.util.Map;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

public class KettleUtil {

	public static String executeJob(String ktrPath,Map<String, String> params){
		try {
			TransMeta transMeta = new TransMeta(ktrPath);
			Trans trans = new Trans(transMeta);
			if (null != params && params.size() > 0) {
				for (Iterator<String> i = params.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					System.out.println(key+" -- "+params.get(key));
					trans.setVariable(key,params.get(key)); 
				}
			}
			execute(trans);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ktrPath; 
	}
	
	/**
	 * 执行转换
	 * @param initKettleParam
	 * @param ktrFilePath
	 * @return
	 */
	public static boolean runKettleTransfer(Map<String, String> initKettleParam, String ktrFilePath) {
		Trans trans = null;
		try {
			// 初始化
			KettleEnvironment.init();
			EnvUtil.environmentInit();
			TransMeta transMeta = new TransMeta(ktrFilePath);
			// 转换
			trans = new Trans(transMeta);
			// 初始化trans参数，脚本中获取参数值：${variableName}
			if (initKettleParam != null) {
				for (String variableName : initKettleParam.keySet()) {
					trans.setVariable(variableName, initKettleParam.get(variableName));
				}
			}
			// 执行转换
			trans.execute(null);
			// 等待转换执行结束
			trans.waitUntilFinished();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static void execute(Trans trans) {
		try {
			trans.execute(null);
			trans.waitUntilFinished();
			if ( trans.getErrors() > 0 )
			{
				throw new RuntimeException( "There were errors during transformation execution." );
			}
		} catch (KettleException e) {
			e.printStackTrace();
		} 
	}

}
