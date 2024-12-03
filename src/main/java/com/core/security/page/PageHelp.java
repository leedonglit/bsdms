package com.core.security.page;



public class PageHelp<T> {
 

	public static <T> String getOrderby(AutoPage<T> page){
		String orderby = "";
		if (page.getSort() != null) {
			orderby = " " + page.getSort() + " " + page.getDir();
		}else{
			orderby = "";
		}
		return orderby;
	}



	public static <T> String getWhere(AutoPage<T> page){
		String whereContent = "";
		if (page.getCondition() != null) {
			for (ColDefine col : page.getCondition()) {
				if (col.getValue()[0] != null && col.getType()!=null) {
					if (whereContent.length() > 0) {
						whereContent = whereContent + " and ";
					}
					if ("like".equalsIgnoreCase(col.getType())) {
						whereContent += col.getCol() +" "+  col.getType() + " '%" + col.getValue()[0] + "%'";
						//paramsList.add("%" + col.getValue()[0] + "%");
					} else if ("in".equalsIgnoreCase(col.getType())) {
						//whereContent += col.toParamValue();
						whereContent += col.getCol() +" "+  col.getType() + " in(" + col.getValue()[0] + ")";
					} else {
						/*whereContent += col.toParamValue();
						if("Date".equalsIgnoreCase(col.getValueType()))
							if ("<=".equalsIgnoreCase(col.getType())) {
								paramsList.add(WebDate.curDayLast(col.getValue()[0]));
							}else {
								paramsList.add(WebDate.convert(col.getValue()[0]));
							}
						else {*/
						if ("String".equalsIgnoreCase(col.getValueType()) || "java.lang.String".equalsIgnoreCase(col.getValueType())) {
							whereContent += col.getCol() +" "+  col.getType() + " '" +col.getValue()[0] +"'";
						}else {
							whereContent += col.getCol() +" "+  col.getType() + " " +col.getValue()[0]  ;
						}
						//}
						//paramsList.add(col.getValue()[0]);
					}

					/*} else {
						String cols[] = col.getCol().split(",");
						if (whereContent.length() > 0) {
							whereContent = whereContent + " and ";
						}
						if (cols.length > 0)
							for (int i = 0; i < cols.length; i++) {
								if (i == 0)
									whereContent += "(";
								if (i > 0)
									whereContent += " or ";

								if ("like".equalsIgnoreCase(col.getType())) {
									whereContent += col.toParamValue(i);
									paramsList.add("%"
											+ col.getValue()[0].trim() + "%");
								} else if ("in".equalsIgnoreCase(col.getType())) {
									System.out.println(col.toParamValue(i));
									whereContent += col.toParamValue(i);
									whereContent += " in("
											+ col.getValue()[0].trim() + ")";
								} else {
									whereContent += col.toParamValue();
									paramsList.add(col.getValue()[0]);
								}
								if (i == cols.length - 1)
									whereContent += " )";
							}
					}*/
				}
			}
		}
		return whereContent;
	}


}
