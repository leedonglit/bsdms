package com.core.security.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AutoPage<T> extends Page<T>{
	                           //模板返回html
    private ColDefine[]           condition       = new ColDefine[0];             //条件
   
    private String                opcode          = "";                           //操作码
          
    public void removeCondition(String col) {
        ArrayList<ColDefine> al = new ArrayList<ColDefine>();
        for (int i = 0; i < condition.length; i++)
            if (condition[i].col.equals(col) == false)
                al.add(condition[i]);
        condition = al.toArray(new ColDefine[al.size()]);
    }
    public void removeCondition(ColDefine col) {
        ArrayList<ColDefine> al = new ArrayList<ColDefine>();
        for (int i = 0; i < condition.length; i++)
            if (condition[i] != col)
                al.add(condition[i]);
        condition = al.toArray(new ColDefine[al.size()]);
    }
    public ColDefine getCondition(String col) {
        for (int i = 0; i < condition.length; i++)
            if (condition[i].col.equals(col) == true)
                return condition[i];
        return null;
    }
    public List<ColDefine> getConditions(String col) {
        ArrayList<ColDefine> al = new ArrayList<ColDefine>();
        for (int i = 0; i < condition.length; i++)
            if (condition[i].col.equals(col) == true)
                al.add(condition[i]);
        return al;
    }
    
    public boolean hasConditionValue(String col) {
        for (int i = 0; i < condition.length; i++)
            if (condition[i].col.equals(col) == true) {
                String[] values = condition[i].value;
                if (values == null || values.length == 0)
                    return false;
                if (values[0] == null || values[0].equals("") == true)
                    return false;
                return true;
            }
        return false;
    }
    public void setConditionValueType(String col, String type) {
        for (int i = 0; i < condition.length; i++)
            if (condition[i].col.equals(col) == true)
                condition[i].valueType = type;
    }
    
    public void addCondition(String col, String type, String value) {
        ColDefine cd = new ColDefine();
        cd.setCol(col);
        cd.setType(type);
        cd.setValue(new String[] { value });
        List<ColDefine> al = new ArrayList<ColDefine>(Arrays.asList(condition));
        al.add(cd);
        condition = al.toArray(new ColDefine[al.size()]);
    }
    /**
     * 把前端条件转化为Map<String,String[]> 
     * @return
     */
    public Map<String, String[]> toMap() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        for (ColDefine c : condition) {
            map.put(c.col, c.value);
        }
        return map;
    }
    public Map<String, String> toOneMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (ColDefine c : condition) {
            if (c.value != null && c.value.length > 0)
                map.put(c.col, c.value[0]);
        }
        return map;
    }
	public ColDefine[] getCondition() {
		return condition;
	}
	public void setCondition(ColDefine[] condition) {
		this.condition = condition;
	}
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
    
}
