//package com.core.extend.excel;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//
//public class ExcelReadTool {
//
//	public static List<Map<Integer, String>> readExcel(InputStream path) {
//		// 定义一个用于存储读取数据的列表
//		List<Map<Integer, String>> dataList = new ArrayList<>();
//		// 使用EasyExcel进行数据读取
//		EasyExcel.read(path, new AnalysisEventListener<Map<Integer, String>>() {
//			@SuppressWarnings("deprecation")
//			@Override
//			public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
//				rowData.put(999, context.getCurrentSheet().getSheetName());
//				// 处理每一行数据
//				dataList.add(rowData);
//			}
//			@Override
//			public void doAfterAllAnalysed(AnalysisContext context) {
//			}
//		}).doReadAll();
//
//		return dataList;
//	}
//
//}
