package com.core.security.page;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WebDate extends Date {
	
	
	public static final String format1 = "yyyy-MM-dd";
	public static final String format2 = "yyyy-MM-dd HH:mm:ss";
	public static final String format3 = "HH:mm:ss";
	public static final String format4 = "yyyy-MM-dd HH";
	public static final String format5 = "yyyyMMddHHmmss";
	public static final String format6 = "yyyy年MM月dd日";
    public static void main2(String args[]) {}
    private static final long serialVersionUID = 123211978171L;
    public WebDate(long value) {
        super(value);
    }
    public WebDate() {}
    public WebDate(String s) {
    	
        this(parseJH(s));   
    }
    public int getNtime() {
        Long t = getTime();
        t = t / 1000;
        return t.intValue();
    }
    static private long parseJH(String time) {
    	if(time==null)return 0;
        try {
            long t = Long.parseLong(time);
            if (t < System.currentTimeMillis() / 100) {
                t = t * 1000;
            }
            if (t > System.currentTimeMillis() / 100)
                return t;
        } catch (Exception e) {}
        return parseLong(time);
    }
    static public long parseLong(Date time) {
        if (time != null)
            return time.getTime();
        return 0;
    }
    static public long parseLong(String time) {
    	if(time==null)return 0;
        time = time.trim();
        // 20100426 2010-04-27 April 22, 2010 April 29, 2010 2010-05-17 22:30
        // 08:26 May 18 2010 2010-05-19 11:17
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format6 = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat format5 = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat format4 = new SimpleDateFormat("HH:mm MMM dd yyyy", Locale.ENGLISH);
        SimpleDateFormat format13 = new SimpleDateFormat("HH:mm, MMM dd, yyyy", Locale.ENGLISH);
        // "09:47, August 19, 2010"
        SimpleDateFormat format7 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        SimpleDateFormat format8 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format9 = new SimpleDateFormat("yyyyMM/dd");
        SimpleDateFormat format10 = new SimpleDateFormat("yyyyMM-dd");
        SimpleDateFormat format11 = new SimpleDateFormat("yyyy-MMdd");
        SimpleDateFormat format12 = new SimpleDateFormat("yyyy/MMdd");
        SimpleDateFormat[] formats = new SimpleDateFormat[] { format0, format1, format2, format3, format4, format5, format6, format7, format8, format9, format10, format11, format13, format12 };
        for (SimpleDateFormat format : formats)
            try {
                return format.parse(time).getTime();
            } catch (Exception e) {}
            
        return 0;
    }
    
    /**
	 * 字符串转化成日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date convert(String date) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format1);
		try {
			retValue = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retValue;
	}
    
	 /**
	 * 字符串转化成日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date convert(String date,String format) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			retValue = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retValue;
	}
	/*
    *//**
     * @param args
     * @throws ParseException 
     *//*
    public static void main(String[] args) throws ParseException {
        // 20100426 2010-04-27 April 22, 2010 April 29, 2010 2010-05-17 22:30
        // 08:26 May 18 2010 2010-05-19 11:17
        
       // Calendar cal = Calendar.getInstance();
//        int day = cal.get(Calendar.DATE);
//        int month = cal.get(Calendar.MONTH) + 1;
//        int year = cal.get(Calendar.YEAR);
//        int dow = cal.get(Calendar.DAY_OF_WEEK);
//        int dom = cal.get(Calendar.DAY_OF_MONTH);
//        int doy = cal.get(Calendar.DAY_OF_YEAR);
    	System.out.println(WebDate.convert("2012-11-21"));
    	SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String s = bartDateFormat.format(new Date());
    	
    	System.out.println(bartDateFormat.parse(s));

    }*/
    public String toString() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return bartDateFormat.format(this);
    }
    public String toCString() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return bartDateFormat.format(this);
    }
    public String toTString() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("HH:mm:ss");
        return bartDateFormat.format(this);
    }
    public String toHString() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd");
        return bartDateFormat.format(this);
    }
    public String toMString() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("HH:mm");
        return bartDateFormat.format(this);
    }
    public String toCDString() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return bartDateFormat.format(this);
    }
    public String toCDateStr() {
        // 200710/19
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd/");
        return bartDateFormat.format(this);
    }
    public String toDateStr() {
        // 200710/19
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return bartDateFormat.format(this);
    }
    public final String toPathStr() {
        // 200710/19
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMM/dd");
        return bartDateFormat.format(this);
    }
    public final String toPathStr2() {
        // 200710/19
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        return bartDateFormat.format(this);
    }
    
    public static Date toDate(Date date,String format){
    	SimpleDateFormat Format = new SimpleDateFormat(format);
    	String s = Format.format(date);
    	try {
			return Format.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static Date toDate(String time, String format) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
        try {
            return bartDateFormat.parse(time);
        } catch (ParseException e) {
            // 
            e.printStackTrace();
        }
        return null;
    }
    
    public  int getYear(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(this);
        int year = cal.get(Calendar.YEAR);
        return year;
    }
    
    public  int getMonth(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(this);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }
    
    public  int getDay(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(this);
        int day = cal.get(Calendar.DATE);
        return day;
    }
    

    public static Date curDayLast (String date) {
    	DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date d2 = null;
		try {
			d2 = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int dayMis=1000*60*60*24;//一天的毫秒-1
		long curMillisecond=d2.getTime();//当天的毫秒
		long resultMis=curMillisecond+(dayMis-1); //当天最后一秒
		Date resultDate=new Date(resultMis);
		return resultDate;
	}
    
}
