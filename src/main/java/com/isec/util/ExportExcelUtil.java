package com.isec.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportExcelUtil {

	private static final String SUB_START = "<sub>";
	private static final String SUB_END = "</sub>";
	private static final String SUP_START = "<sup>";
	private static final String SUP_END = "</sup>";


	/**
	 * 
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	public static void exportExcel(String title, String[] headers,List<String[]> dataset, OutputStream out) {
		// 声明一个工作薄
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("idgar");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<String[]> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			String[] t = (String[]) it.next();
			for (short i = 0; i < t.length; i++) {
				HSSFCell cell = row.createCell(i);
				// 判断值的类型后进行强制类型转换
				String textValue = t[i];
				List<List<int[]>> tagIndexArr = null;
				if (containSubSup(textValue)) {
					tagIndexArr = new ArrayList<List<int[]>>();
					textValue = getSubSupIndexs(textValue, tagIndexArr);
				}

				if (tagIndexArr != null) {
					HSSFRichTextString text = new HSSFRichTextString(textValue);
					List<int[]> subs = tagIndexArr.get(0);
					List<int[]> sups = tagIndexArr.get(1);
					if (subs.size() > 0) {
						HSSFFont ft = workbook.createFont();
						ft.setTypeOffset(HSSFFont.SS_SUB);
						for (int[] pair : subs) {
							text.applyFont(pair[0], pair[1], ft);
						}
					}
					if (sups.size() > 0) {
						HSSFFont ft = workbook.createFont();
						ft.setTypeOffset(HSSFFont.SS_SUPER);
						for (int[] pair : sups) {
							text.applyFont(pair[0], pair[1], ft);
						}
					}
					cell.setCellValue(text);
				}else {
					cell.setCellValue(textValue);
				}

				//				if (!StringUtils.isEmpty(textValue) && textValue.indexOf("sub")>-1) {
				//					HSSFFont ft = workbook.createFont();
				//					ft.setTypeOffset(HSSFFont.SS_SUB);
				//					for (int[] pair : subs) {
				//						richString.applyFont(pair[0], pair[1], ft);
				//					}
				//				}else  if (!StringUtils.isEmpty(textValue) && textValue.indexOf("sup")>-1) {
				//					HSSFFont ft = workbook.createFont();
				//					ft.setTypeOffset(HSSFFont.SS_SUPER);
				//					for (int[] pair : sups) {
				//						richString.applyFont(pair[0], pair[1], ft);
				//					}
				//				}

			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 * 获取下一对标签的index，不存在这些标签就返回null
	 * @param s
	 * @param tag SUB_START 或者SUP_START
	 * @return int[]中有两个元素，第一个是开始标签的index，第二个元素是结束标签的index
	 */
	public static int[] getNextSubsTagsIndex(String s, String tag) {

		int firstSubStart = s.indexOf(tag);
		if (firstSubStart > -1) {
			int firstSubEnd = s.indexOf(tag.equals(SUB_START) ? SUB_END : SUP_END);
			if (firstSubEnd > firstSubStart) {
				return new int[] { firstSubStart, firstSubEnd };
			}
		}
		return null;
	}

	/**移除下一对sub或者sup标签，返回移除后的字符串
	 * @param s
	 * @param tag SUB_START 或者SUP_START
	 * @return
	 */
	public static String removeNextSubTags(String s, String tag) {
		s = s.replaceFirst(tag, "");
		s = s.replaceFirst(tag.equals(SUB_START) ? SUB_END : SUP_END, "");
		return s;
	}

	/**
	 * 判断是不是包含sub，sup标签
	 * @param s
	 * @return
	 */
	public static boolean containSubSup(String s) {
		return (s.contains(SUB_START) && s.contains(SUB_END)) || (s.contains(SUP_START) && s.contains(SUP_END));
	}



	/**
	 * 处理字符串，得到每个sub，sup标签的开始和对应的结束的标签的index，方便后面根据这个标签做字体操作
	 * @param s
	 * @param tagIndexList 传一个新建的空list进来，方法结束的时候会存储好标签位置信息。
	 * <br>tagIndexList.get(0)存放的sub
	 * <br>tagIndexList.get(1)存放的是sup
	 * 
	 * @return 返回sub，sup处理完之后的字符串
	 */
	public static String getSubSupIndexs(String s, List<List<int[]>> tagIndexList) {
		List<int[]> subs = new ArrayList<int[]>();
		List<int[]> sups = new ArrayList<int[]>();

		while (true) {
			int[] sub_pair = getNextSubsTagsIndex(s, SUB_START);
			int[] sup_pair = getNextSubsTagsIndex(s, SUP_START);
			boolean subFirst = true;
			boolean supFirst = true;
			if(sub_pair != null && sup_pair != null) {
				//两种标签都存在的时候要考虑到谁在前，在前的标签优先处理
				//因为如果在后的标签处理完，index就定下来，再处理在前的，后面的index就会产生偏移量。从前开始处理不会存在这个问题
				if(sub_pair[0] < sup_pair[0]) {
					supFirst = false;
				} else {
					subFirst = false;
				}
			}
			if (sub_pair != null && subFirst) {
				s = removeNextSubTags(s, SUB_START);
				//<sub>标签被去掉之后，结束标签需要相应往前移动
				sub_pair[1] = sub_pair[1] - SUB_START.length();
				subs.add(sub_pair);
				continue;
			}
			if (sup_pair != null && supFirst) {
				s = removeNextSubTags(s, SUP_START);
				//<sup>标签被去掉之后，结束标签需要相应往前移动
				sup_pair[1] = sup_pair[1] - SUP_START.length();
				sups.add(sup_pair);
				continue;
			}
			if (sub_pair == null && sup_pair == null) {
				break;
			}
		}

		tagIndexList.add(subs);
		tagIndexList.add(sups);
		return s;
	}

}
