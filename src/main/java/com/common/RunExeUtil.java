package com.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.core.tools.PropertiesTool;

public class RunExeUtil  {
	
	public static void  agilesatelliteExe() throws Exception{
		try{
			String dllfile = PropertiesTool.getString("taskplanexe.path");
			String line;
			Process p=Runtime.getRuntime().exec(("cmd /c agilesatellite.exe"),null,new File(dllfile));
			BufferedReader br= new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void  areastatisticsExe() throws Exception {
		try{
			String dllfile = PropertiesTool.getString("areaexe.path");
			String line;
			Process p=Runtime.getRuntime().exec(("cmd  /c areastatistics.exe "),null,new File(dllfile));
			BufferedReader br= new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
