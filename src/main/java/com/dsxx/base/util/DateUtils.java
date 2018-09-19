package com.dsxx.base.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    public static String PATTERN_DATE = "yyyy-MM-dd";
    public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_MINTIME = "yyyy-MM-dd HH:mm";
    public static String PATTERN_MONTH_DATE = "MM-dd";
    public static String PATTERN_MONTH = "yyyy-MM";
    public static String PATTERN_CHAR = "yyyy年MM月dd日";
    public static String PATTERN_DATE_POINT = "yyyy.MM.dd";

    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    // do nothing
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        } else {
            // monthNow<monthBirth
            // donothing
        }

        return age;
    }

    public static String getCurrentTime() {
        return dateToString(new Date());
    }

    /**
     * 将javaDate类型的日期转化为 yyyy-MM-dd HH:mm:ss 格式的字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_MINTIME);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 将javaDate类型的日期转化为自定义格式的字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 将字符串格式的时间转化为Date类型的日期，
     *
     * @param dateString
     * @param format
     * @return
     */
    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期多少天之后的日期
     *
     * @param days
     * @return
     */
    public static Date getNextDayByCurrent(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 获取当前日期多少天之前的日期
     *
     * @param days
     * @return
     */
    public static Date getBeforeDayByCurrent(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }

    /**
     * 获取当前日期多少分钟之后的日期
     *
     * @param days
     * @return
     */
    public static Date getNextTimeByDate(Date date, int minuts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minuts);
        return calendar.getTime();
    }

    /**
     * 获取当前日期多少分钟之后的日期
     *
     * @param days
     * @return
     */
    public static Date getNextTimeBySecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 计算两个日期的天差
     *
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getDayBetweenDate(Date beforDate, Date afterDate) {
        //统一转换成 yyyy-MM-dd 格式
        beforDate = stringToDate(dateToString(beforDate, PATTERN_DATE), PATTERN_DATE);
        afterDate = stringToDate(dateToString(afterDate, PATTERN_DATE), PATTERN_DATE);
        return (afterDate.getTime() - beforDate.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 计算两个日期的时差
     *
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getHourBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000 * 60 * 60);
    }

    /**
     * 计算两个日期的分差
     *
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getMinuteBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000 * 60);
    }

    /**
     * 计算两个日期的秒差
     *
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getSecondBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000);
    }

    public static String getCurrenYearAndWeek() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR); // 获取年
        int week = c.get(Calendar.WEEK_OF_YEAR);// 获取当前的周
        if (week < 10) {
            return year + "0" + c.get(Calendar.WEEK_OF_YEAR);
        } else {
            return year + "" + c.get(Calendar.WEEK_OF_YEAR);
        }
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDayStart() {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String beginDate = formatter.format(new Date(System.currentTimeMillis()));
        return beginDate + " 00:00:00";
    }

    /**
     * 获取日期的当天开始时间
     *
     * @return
     */
    public static Date getDayStart(Date day) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String beginDate = formatter.format(day);
        try {
            Date date = formatter.parse(beginDate);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期的当天结束时间
     *
     * @return
     */
    public static Date getDayEnd(Date day) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATETIME);
        try {
            Date date = format.parse(getDayEndStr(day));
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期的当天开始时间字符串
     *
     * @return
     */
    public static String getDayStartStr(Date day) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String beginDate = formatter.format(day);
        return beginDate + " 00:00:00";
    }

    public static Date getHourStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getNextTimeByHour(Date date, int hrs) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hrs);
        return c.getTime();
    }

    /**
     * @param
     * @return
     * @Description: 日期加减多少个月
     * @author liuyang
     * @date 2017年8月17日 下午5:37:41
     */
    public static Date getMonthDay(Date date, int count) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, count);
        return c.getTime();
    }

    /**
     * 获取时间的当天开始时间字符串
     *
     * @return
     */
    public static String getBeforTime(int hour) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -hour);
        return dateToString(c.getTime());
    }

    /**
     * 获取日期的当天结束时间字符串
     *
     * @return
     */
    public static String getDayEndStr(Date day) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String endDate = formatter.format(day);
        return endDate + " 23:59:59";
    }

    /**
     * 获取距离当天的截止时间还有多少秒
     *
     * @return
     */
    public static int getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        time = calendar.getTimeInMillis() - time;
        return (int) (time / 1000);// 距离第二天还有多少秒
    }

    /**
     * 获取昨天的日期格式 09-25
     *
     * @param days
     * @return
     */
    public static String getYesterDayFormart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        return formatter.format(calendar.getTime());
    }

    /**
     * 获取多久以前的时间
     *
     * @param minAge
     * @param field
     * @return
     */
    public static Date getPreviousDate(int years, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, -years);
        return calendar.getTime();
    }

    /**
     * 计算两个日期的年差
     *
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static int getYearBetweenDate(Date beforDate, Date afterDate) {
        if (afterDate.before(beforDate)) {
            // 如果afterDate比before还大，则互换两个时间变量的值
            Date temp = afterDate;
            afterDate = beforDate;
            beforDate = temp;
        }
        int year = afterDate.getYear() - beforDate.getYear();
        if (afterDate.getMonth() < beforDate.getMonth()
                || (afterDate.getMonth() == beforDate.getMonth() && afterDate.getDay() < beforDate.getDay())) {
            year--;
        }
        return year;
    }

    /**
     * 获取一天的开始时间
     *
     * @param dateStr
     * @return
     */
    public static Date getDayStart(String dateStr) {
        return stringToDate(getDayStartStr(dateStr), PATTERN_DATETIME);
    }

    /**
     * 获取一天的开始时间
     *
     * @param dateStr
     * @return
     */
    public static Date getDayEnd(String dateStr) {
        return stringToDate(getDayEndStr(dateStr), PATTERN_DATETIME);
    }

    /**
     * 获取某一天的开始
     *
     * @param dataStr
     * @return
     */
    public static String getDayStartStr(String dateStr) {
        String date = getDayStr(dateStr);
        if (!StringUtils.isBlank(date)) {
            return date + " 00:00:00";
        }
        return null;
    }

    /**
     * 获取某一天的结束
     *
     * @param dataStr
     * @return
     */
    public static String getDayEndStr(String dateStr) {
        String date = getDayStr(dateStr);
        if (!StringUtils.isBlank(date)) {
            return date + " 23:59:59";
        }
        return null;
    }

    /**
     * 获取一天的日期
     *
     * @param dateStr
     * @return
     */
    public static String getDayStr(String dateStr) {
        if (!StringUtils.isBlank(dateStr)) {
            Matcher m1 = Pattern.compile("(?<date>\\d{4}-\\d{1,2}-\\d{1,2})( \\d{1,2}:\\d{1,2}:\\d{1,2})?")
                    .matcher(dateStr);
            if (m1.matches()) {
                String date = m1.group("date");
                return date;
            }
        }
        return null;
    }

    /**
     * 获取上个月的第一天
     *
     * @return
     */
    public static String getFirstDayOfLastMonth(String patern) {
        SimpleDateFormat format = new SimpleDateFormat(patern);

        // 获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();// 获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String firstDay = format.format(cal_1.getTime());
        return firstDay;

    }

    /**
     * 获取上个月的最后一天
     *
     * @return
     */
    public static String getLastDayOfLastMonth(String patern) {
        SimpleDateFormat format = new SimpleDateFormat(patern);
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为0号,当前日期既为本月最后一天
        String lastDay = format.format(cale.getTime());
        return lastDay;
    }

    public static String getFirstDayOfThisMonth(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }

    /**
     * @param
     * @return
     * @Description: 根据日期获取当前月份的第一天
     * @author liuyang
     * @date 2017年8月17日 下午5:31:32
     */
    public static String getFirstDayOfMonth(Date day, String pattern) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String first = format.format(c.getTime());
        return first;
    }

    /**
     * 根据日期获取当前月份的最后一天
     *
     * @param day
     * @param patern
     * @return
     * @author liuyang
     * @date 2017年10月13日下午2:33:54
     */
    public static String getLastDayOfMonth(Date day, String patern) {
        SimpleDateFormat format = new SimpleDateFormat(patern);
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(day);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为0号,当前日期既为本月最后一天
        String lastDay = format.format(cale.getTime());
        return lastDay;
    }

    /**
     * @param time
     * @return
     * @Description:将时间戳转换为时间
     * @author wzw
     * @Date 2017年8月3日
     */
    public static Date longToDate(long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String formatStr = format.format(time);
        Date date = null;
        try {
            date = format.parse(formatStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 功能描述: 将时间戳转换成时间字符串<br>
     *
     * @Author: wzw
     * @Date: 2018/1/12 17:49
     * @param:
     * @return:
     */
    public static String longToDateStr(long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String formatStr = format.format(time);
        return formatStr;
    }

    /**
     * @param
     * @return
     * @Description: 获取该日期所在月份的天数
     * @author liuyang
     * @date 2017年8月21日 上午11:48:53
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param
     * @return
     * @Description: 日期加或者减多少天
     * @author liuyang
     * @date 2017年8月21日 下午2:52:14
     */
    public static Date getNextDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 获取某个月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取某个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }

    /**
     * 获取昨天
     *
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取上个月
     *
     * @return
     */
    public static Date getLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 时分格式转换成时分秒格式
     *
     * @param time
     * @return
     * @author liuyang
     * @date 2017年9月12日下午2:02:35
     */
    public static String getSecondTime(String time) {
        if (StringUtils.isBlank(time)) {
            return time;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        if (time.split(":").length == 2) {
            //时分格式，后面加上秒
            time += ":00";
        }
        try {
            time = sdf.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 遍历两个日期之间的所有日期
     *
     * @param start      开始日期
     * @param end        结束日期
     * @param dateFormat 返回日期的字符串格式
     * @return List 返回这个时间段内每天的日期集合,包括开始日期和结束日期。不会返回null
     * @author slm
     */
    public static List<String> getDays(Date start, Date end, String dateFormat) {
        List<String> list = new ArrayList<>();
        try {
            // 将两个日期转换为 yyyy-MM-dd
            String startStr = dateToString(start, PATTERN_DATE);
            String endStr = dateToString(end, PATTERN_DATE);
            start = stringToDate(startStr, PATTERN_DATE);
            end = stringToDate(endStr, PATTERN_DATE);

            if (start.getTime() > end.getTime()) {
                throw new RuntimeException("开始日期大于结束日期");
            }

            Date tmp = start;
            while (!tmp.equals(end)) {
                list.add(dateToString(tmp, dateFormat));
                tmp = org.apache.commons.lang.time.DateUtils.addDays(tmp, 1);
            }
            list.add(dateToString(end, dateFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 填充每日数据<br/>
     * 比如查询一个月的数据，结果只有15号到月底的每天数据，这个方法就是把没有数据的那些天填充为0
     *
     * @param startDate
     * @param endDate
     * @param resultList
     * @return
     */
    public static List<Object> fillDailyData(Date startDate, Date endDate, List<Map<String, Object>> resultList) {
        // xAxis对应的收入
        List<Object> result = new ArrayList<>();
        // key:日期，value:key日期所对应的收入
        Map<String, Object> resultListMap = new HashMap<>(2);
        List<String> dates = DateUtils.getDays(startDate, endDate, DateUtils.PATTERN_DATE);
        // 将List转Map
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> map : resultList) {
                // 日期
                String key = "";
                // 这个日期所对应的收入
                Object value = null;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("balanceDay".equalsIgnoreCase(entry.getKey())) {
                        key = entry.getValue() + "";
                    } else {
                        value = entry.getValue() + "";
                    }
                }
                resultListMap.put(key, value);
            }
        }
        for (String date : dates) {
            Object object = resultListMap.get(date);
            result.add(object == null ? 0 : object);
        }
        return result;
    }

    /**
     * 功能描述: 获取当前时间到未来一个时间点的倒计时 <br>
     * @Author: wzw
     * @Date: 2018/5/23 14:43
     * @return xx天xx时xx分xx秒
     */
    public static String getCountDown(Date futureTime){
        Date currentTime = new Date(System.currentTimeMillis());
        if(currentTime.after(futureTime)){
            return "0天0时0分0秒";
        }
        long currentMillis = currentTime.getTime();
        long futureMillis = futureTime.getTime();
        long period = (futureMillis-currentMillis)/1000;
        //天
        long day = period/(60*60*24);
        period = period-day*24*60*60;
        //时
        long hour = period/(60*60);
        period = period - hour*60*60;
        //分
        long min = period/60;
        //秒
        long sec = period - min*60;
        return day+"天"+hour+"时"+min+"分"+sec+"秒";
    }
}
