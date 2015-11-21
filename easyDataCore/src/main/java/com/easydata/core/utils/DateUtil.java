package com.easydata.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期类型与字符串类型相互转换。<br />
 *
 * @author Mr.Pro
 */
public class DateUtil {
	/**
	 * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of
	 * December in the year 2002
	 */
	public static final String ISO_DATE_FORMAT = "yyyyMMdd";

	/**
	 * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th
	 * day of December in the year 2002
	 */
	public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

	public static final String CONVERSE_DATETIME_PATTERN = "dd-MM-yy";

	public static final String JAVASCRIPT_DATETIME_PATTERN = "yyyy/MM/dd HH:mm:ss";
	/**
	 * 24小时制 yyyy-MM-dd HH:mm:ss
	 */
	public static String DATETIME_PATTERN24H = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 12小时制 yyyy-MM-dd hh:mm:ss
	 */
	public static String DATETIME_PATTERN12H = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 12小时制 yyyy-MM-dd hh:mm:ss
	 */
	public static String DATETIME_PATTERN24H_NO_HYPHEN = "yyyyMMddHHmmss";

	public static String DATETIME_PATTERN24H_NO_HYPHENSS = "yyyyMMddHHmmssSSSS";

	/**
	 * Default lenient setting for getDate.
	 */
	private static boolean LENIENT_DATE = false;

	/**
	 * 把yyyy-mm-dd的时间字符串转换成dd-mm-yyyy格式的时间字符串
	 */
	public static String getConverseDateString(String dateString) throws ParseException{

		return stringToString(dateString, ISO_EXPANDED_DATE_FORMAT,
				CONVERSE_DATETIME_PATTERN);
	}

	/**
	 * Returns the days between two dates. Positive values indicate that the
	 * second date is after the first, and negative values indicate, well, the
	 * opposite. Relying on specific times is problematic.
	 * 
	 * @param early
	 *            the "first date"
	 * @param late
	 *            the "second date"
	 * @return the days between the two dates
	 */
	public static final int daysBetween(Date early, Date late) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(early);
		c2.setTime(late);

		return daysBetween(c1, c2);
	}

	/**
	 * Returns the days between two dates. Positive values indicate that the
	 * second date is after the first, and negative values indicate, well, the
	 * opposite.
	 * 
	 * @param early
	 * @param late
	 * @return the days between two dates.
	 */
	public static final int daysBetween(Calendar early, Calendar late) {

		return (int) (toJulian(late) - toJulian(early));
	}

	/**
	 * Return a Julian date based on the input parameter. This is based from
	 * calculations found at <a
	 * href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day
	 * Calculations (Gregorian Calendar) </a>, provided by Bill Jeffrys.
	 * 
	 * @param c
	 *            a calendar instance
	 * @return the julian day number
	 */
	public static final float toJulian(Calendar c) {

		int Y = c.get(Calendar.YEAR);
		int M = c.get(Calendar.MONTH);
		int D = c.get(Calendar.DATE);
		int A = Y / 100;
		int B = A / 4;
		int C = 2 - A + B;
		float E = (int) (365.25f * (Y + 4716));
		float F = (int) (30.6001f * (M + 1));
		float JD = C + D + E + F - 1524.5f;

		return JD;
	}

	/**
	 * 日期增加
	 * 
	 * @param isoString
	 *            日期字符串
	 * @param fmt
	 *            格式
	 * @param field
	 *            年/月/日 Calendar.YEAR/Calendar.MONTH/Calendar.DATE
	 * @param amount
	 *            增加数量
	 * @return
	 * @throws ParseException
	 */
	public static final String dateIncrease(String isoString, String fmt,
			int field, int amount) throws ParseException{

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(stringToDate(isoString, fmt, true));
		cal.add(field, amount);

		return dateToString(cal.getTime(), fmt);
	}

	/**
	 * Time Field Rolling function. Rolls (up/down) a single unit of time on the
	 * given time field.
	 * 
	 * @param isoString
	 * @param field
	 *            the time field.
	 * @param up
	 *            Indicates if rolling up or rolling down the field value.
	 * @param expanded
	 *            use formating char's
	 * @exception ParseException
	 *                if an unknown field value is given.
	 */
	public static final String roll(String isoString, String fmt, int field,
			boolean up) throws ParseException {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(stringToDate(isoString, fmt));
		cal.roll(field, up);

		return dateToString(cal.getTime(), fmt);
	}

	/**
	 * Time Field Rolling function. Rolls (up/down) a single unit of time on the
	 * given time field.
	 * 
	 * @param isoString
	 * @param field
	 *            the time field.
	 * @param up
	 *            Indicates if rolling up or rolling down the field value.
	 * @exception ParseException
	 *                if an unknown field value is given.
	 */
	public static final String roll(String isoString, int field, boolean up)
			throws ParseException {

		return roll(isoString, DATETIME_PATTERN24H, field, up);
	}

	/**
	 * 字符串转换为日期java.util.Date
	 * 
	 * @param dateText
	 *            字符串
	 * @param format
	 *            日期格式
	 * @param lenient
	 *            日期越界标志
	 * @return
	 */
	public static Date stringToDate(String dateText, String format,
			boolean lenient) throws ParseException{

		if (dateText == null) {

			return null;
		}

		DateFormat df = null;


		if (format == null) {
			df = new SimpleDateFormat();
		} else {
			df = new SimpleDateFormat(format);
		}

		// setLenient avoids allowing dates like 9/32/2001
		// which would otherwise parse to 10/2/2001
		df.setLenient(false);
		df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

		return df.parse(dateText);
	}

	/**
	 * 字符串转换为日期java.util.Date
	 * 
	 * @param dateText
	 *            字符串
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static Date mongodbstringToDate(String dateString, String format) throws ParseException{

		return mongodbstringToDate(dateString, format, LENIENT_DATE);
	}

	/**
	 * 字符串转换为日期java.util.Date
	 * 
	 * @param dateText
	 *            字符串
	 * @param format
	 *            日期格式
	 * @param lenient
	 *            日期越界标志
	 * @return
	 */
	public static Date mongodbstringToDate(String dateText, String format,
			boolean lenient) throws ParseException{

		if (dateText == null) {

			return null;
		}

		DateFormat df = null;

		if (format == null) {
			df = new SimpleDateFormat();
		} else {
			df = new SimpleDateFormat(format);
		}

		// setLenient avoids allowing dates like 9/32/2001
		// which would otherwise parse to 10/2/2001
		df.setLenient(false);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		return df.parse(dateText);
	}

	/**
	 * 字符串转换为日期java.util.Date
	 * 
	 * @param dateText
	 *            字符串
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static Date stringToDate(String dateString, String format) throws ParseException{

		return stringToDate(dateString, format, LENIENT_DATE);
	}

	/**
	 * 字符串转换为日期java.util.Date
	 * 
	 * @param dateText
	 *            字符串
	 */
	public static Date stringToDate(String dateString) throws ParseException{

		return stringToDate(dateString, ISO_EXPANDED_DATE_FORMAT, LENIENT_DATE);
	}

	/**
	 * 根据时间变量返回时间字符串
	 * 
	 * @return 返回时间字符串
	 * @param pattern
	 *            时间字符串样式
	 * @param date
	 *            时间变量
	 */
	public static String dateToString(Date date, String pattern) {

		if (date == null) {

			return null;
		}


		SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
		sfDate.setLenient(false);

		return sfDate.format(date);
	}

	/**
	 * 根据时间变量返回时间字符串 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
	}

	/**
	 * 返回当前时间
	 * 
	 * @return 返回当前时间
	 */
	public static Date getCurrentDateTime() {
		Calendar calNow = Calendar.getInstance();
		Date dtNow = calNow.getTime();

		return dtNow;
	}

	/**
	 * 返回当前日期字符串
	 * 
	 * @param pattern
	 *            日期字符串样式
	 * @return
	 */
	public static String getCurrentDateString(String pattern) {
		return dateToString(getCurrentDateTime(), pattern);
	}

	/**
	 * 返回当前日期字符串 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDateString() {
		return dateToString(getCurrentDateTime(), ISO_EXPANDED_DATE_FORMAT);
	}

	/**
	 * 返回当前日期+时间字符串 yyyy-MM-dd hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStringWithTime() {

		return dateToString(getCurrentDateTime(), DATETIME_PATTERN24H);
	}

	public static String longTimeString(String mode) {
		return dateToString(getCurrentDateTime(), mode);
	}

	public static String longTimeString() {
		return longTimeString("yyyyMMddHHmmss");
	}

	/**
	 * 返回当前日期+时间字符串 yyyy-MM-dd hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStringWithTime(Date date) {

		return dateToString(date, DATETIME_PATTERN24H);
	}

	/**
	 * 日期增加-按日增加
	 * 
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static Date dateIncreaseByDay(Date date, int days) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 日期增加-按月增加
	 * 
	 * @param date
	 * @param mnt
	 * @return java.util.Date
	 */
	public static Date dateIncreaseByMonth(Date date, int mnt) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.MONTH, mnt);

		return cal.getTime();
	}

	/**
	 * 日期增加-按年增加
	 * 
	 * @param date
	 * @param mnt
	 * @return java.util.Date
	 */
	public static Date dateIncreaseByYear(Date date, int mnt) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.YEAR, mnt);

		return cal.getTime();
	}

	/**
	 * 日期增加-按小时增加
	 * 
	 * @param date
	 *            日期字符串 yyyy-MM-dd HH:mm:ss
	 * @param i
	 *            增加的小时数
	 * @return 日期字符串 yyyy-MM-dd HH:mm:ss
	 */
	public static Date dateIncreaseByHour(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, i);// 分钟加1
		return calendar.getTime();
	}

	/**
	 * 日期增加-按分钟增加
	 * 
	 * @param date
	 *            日期字符串 yyyy-MM-dd HH:mm:ss
	 * @param i
	 *            增加的分钟数
	 * @return 日期字符串 yyyy-MM-dd HH:mm:ss
	 */
	public static Date dateIncreaseByMinute(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, i);// 分钟加1
		return calendar.getTime();
	}

	/**
	 * 日期增加-按秒增加
	 * 
	 * @param date
	 *            日期字符串 yyyy-MM-dd HH:mm:ss
	 * @param i
	 *            增加的秒数
	 * @return 日期字符串 yyyy-MM-dd HH:mm:ss
	 */
	public static Date dateIncreaseBySecond(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, i);// 分钟加1
		return calendar.getTime();
	}

	/**
	 * 日期增加
	 * 
	 * @param date
	 *            日期字符串 yyyy-MM-dd
	 * @param days
	 * @return 日期字符串 yyyy-MM-dd
	 */
	public static String dateIncreaseByDay(String date, int days) throws ParseException {
		return dateIncreaseByDay(date, ISO_DATE_FORMAT, days);
	}

	/**
	 * 日期增加
	 * 
	 * @param date
	 *            日期字符串
	 * @param fmt
	 *            日期格式
	 * @param days
	 * @return
	 */
	public static String dateIncreaseByDay(String date, String fmt, int days) throws ParseException{
		return dateIncrease(date, fmt, Calendar.DATE, days);
	}

	/**
	 * 两日期相减，得到天数
	 * 
	 * @param date
	 *            日期字符串
	 * @param fmt
	 *            日期格式
	 * @param days
	 * @return
	 */
	public static int getIntervalDays(Date enddate, Date begindate) {
		long millisecond = enddate.getTime() - begindate.getTime();
		int day = (int) (millisecond / 24L / 60L / 60L / 1000L);
		return day;
	}

	/**
	 * 两日期相减，得到小时
	 * 
	 * @param date
	 *            日期字符串
	 * @param fmt
	 *            日期格式
	 * @param days
	 * @return
	 */
	public static int getIntervalHours(Date enddate, Date begindate) {
		long millisecond = enddate.getTime() - begindate.getTime();
		int hours = (int) (millisecond / 60L / 60L / 1000L);
		return hours;
	}

	/**
	 * 两日期相减，得到秒
	 * 
	 * @param enddate
	 * @param begindate
	 * @return
	 */
	public static int getIntervalSeconds(Date enddate, Date begindate) {
		long millisecond = enddate.getTime() - begindate.getTime();
		int hours = (int) (millisecond / 1000L);
		return hours;
	}

	/**
	 * 日期字符串格式转换
	 * 
	 * @param src
	 *            日期字符串
	 * @param srcfmt
	 *            源日期格式
	 * @param desfmt
	 *            目标日期格式
	 * @return
	 */
	public static String stringToString(String src, String srcfmt, String desfmt) throws ParseException {
		return dateToString(stringToDate(src, srcfmt), desfmt);
	}

	public static String getYear(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return format.format(date);
	}

	public static String getYear() {
		return getYear(new Date());
	}

	public static String getSeason() {
		return getSeason(new Date());
	}

	public static String getSeason(Date date) {
		String result = null;
		String month = getMonth(date);
		int m = Integer.parseInt(month);
		if (m > 0 && m <= 3)
			result = "1";
		if (m > 3 && m <= 6)
			result = "2";
		if (m > 6 && m <= 9)
			result = "3";
		if (m > 9 && m <= 12)
			result = "4";
		return result;
	}

	public static String getMonth() {
		return getMonth(new Date());
	}

	public static String getMonth(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM");
		return format.format(date);
	}

	public static String getDay() {
		return getDay(new Date());
	}

	public static int getAgeFromBirthDate(String birthDate) throws NumberFormatException{
		if (birthDate.equals(""))
			return 0;
		String[] temp = birthDate.split("-");
		int year = Integer.parseInt(temp[0]);
		int age = Integer.parseInt(getYear()) - year;
		return age;
	}

	public static String getDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd");
		return format.format(date);
	}

	public static boolean isDateFormat(String str) {
		String tmpStr = "";
		char StrI = '\u0000';
		for (int i = 0; i < str.length(); i++) {
			StrI = str.charAt(i);
			if ((StrI >= '0') && (StrI <= '9')) {
				tmpStr += "*";
			} else {
				tmpStr += StrI;
			}
		}
		tmpStr = tmpStr.trim();
		if (tmpStr.equals("****-**-**") || tmpStr.equals("****-*-**")
				|| tmpStr.equals("****-**-*") || tmpStr.equals("****-*-*")) {
			int pos1 = str.indexOf("-");
			int pos2 = str.lastIndexOf("-");
			String s = str.substring(pos1 + 1, pos2);
			if (Integer.parseInt(s) > 12) {
				return false;
			}
			s = str.substring(pos2 + 1);
			if (Integer.parseInt(s) > 31) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @功能：将秒数换算成x时x分x秒x毫秒
	 * 
	 * @修改者：
	 * @修改内容：
	 * @修改时间：
	 * 
	 * @param ms
	 * @return
	 */
	public static String format(long ms) {
		int mi = 60;
		int hh = mi * 60;
//		int dd = hh * 24;

		// long day = ms / dd;
		long hour = (ms) / hh;
		long minute = (ms - hour * hh) / mi;
		long second = (ms - hour * hh - minute * mi);

		// String strDay = day < 10 ? "0" + day : "" + day;
		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		// if (strDay != null && "00".equals(strDay)) {
		return strHour + ":" + strMinute + ":" + strSecond;
		// }
		// return strDay + "天" + strHour + ":" + strMinute + ":" + strSecond;
	}

	/**
	 * 叠加日期
	 * 
	 * @param d
	 * @param amount
	 * @return
	 */
	public static Date increaseDate(Date d, int type, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(type, amount);//
		return calendar.getTime();
	}

	public static String monthParser(Date date) {
		if (date == null)
			return "";
		String m = getMonth(date);
		if ("01".equals(m))
			return "一月";
		if ("02".equals(m))
			return "二月";
		if ("03".equals(m))
			return "三月";
		if ("04".equals(m))
			return "四月";
		if ("05".equals(m))
			return "五月";
		if ("06".equals(m))
			return "六月";
		if ("07".equals(m))
			return "七月";
		if ("08".equals(m))
			return "八月";
		if ("09".equals(m))
			return "九月";
		if ("10".equals(m))
			return "十月";
		if ("11".equals(m))
			return "十一月";
		if ("12".equals(m))
			return "十二月";
		return "";

	}

	/**
	 * 将时间段分面几个时间段
	 * 
	 * @param start
	 *            开始时间
	 * @param len
	 *            时间长度
	 * @param splitlen
	 *            切割长度，单位分钟
	 * @return
	 */
	public static List<Date> splitTime(Date start, int len, int splitlen) {
		if (len == 0 || splitlen == 0) {
			return null;
		}
		List<Date> result = new ArrayList<Date>();
		Date begin = new Date(start.getTime()
				- (start.getTime() % (splitlen * 60 * 1000)));
		int splitNum = (int) Math.ceil((double) len / splitlen);
		for (int i = 0; i < splitNum; i++) {
			result.add(dateIncreaseByMinute(begin, splitlen * i));
		}

		return result;
	}
	
	public static String addMinute(long currentTm, int num){
		String newTm = "";
		Calendar cale = Calendar.getInstance();
		Date date = new Date();
		date.setTime(currentTm);
		cale.setTime(date);
		cale.add(Calendar.MINUTE, num);
		newTm =DateUtil.dateToString(cale.getTime(),DateUtil.JAVASCRIPT_DATETIME_PATTERN);
        return newTm;
	
		}



}