package com.core.lib_core.utils;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yutinglong
 * @version 1.0
 * @description: 字符串处理工具类
 * @created 2014-1-8 下午4:47:26
 * @changeRecord
 */
public class StringUtil {

    /* 时间戳转换成字符窜 */
    public static String getDateToString(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 将服务器返回的1234.50转换成成1,234.50这种形式
    public static String transformPrice(String price) {
        if (TextUtils.isEmpty(price)) {
            return "null";
        }
        String[] parts = price.split("[.]");
        StringBuffer sb = new StringBuffer();
        int length = parts[0].length();
        int remainder = length % 3;
        int partCount = length / 3 + (remainder > 0 ? 1 : 0);
        int currentIndex = 0;
        if (remainder == 0) {// 长度是3的倍数的情况
            for (int i = 0; i < partCount; i++) {
                sb.append(parts[0].substring(currentIndex, currentIndex + 3));
                currentIndex += 3;
                if (i != partCount - 1) {
                    sb.append(",");
                }
            }
        } else {// 长度不是3的倍数的情况
            if (length < 3) {// 长度<3直接返回
                return price;
            } else {
                for (int i = 0; i < partCount; i++) {
                    if (i == 0) {
                        sb.append(parts[0].substring(currentIndex, remainder));
                        currentIndex = remainder;
                    } else {
                        sb.append(parts[0].substring(currentIndex, currentIndex + 3));
                        currentIndex += 3;
                    }
                    if (i != partCount - 1) {
                        sb.append(",");
                    }
                }
            }
        }
        if (parts.length > 1) {// 说明有小数
            sb.append(".");
            sb.append(parts[1]);
        }
        return sb.toString();
    }

    public static String get1(String str, int size, String spliter) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(spliter).append(str);
        }

        if (sb.length() > 0)
            return sb.substring(spliter.length());

        return sb.toString();
    }

    public static String[] getMergeArray(String[] al, String[] bl) {
        String[] a = al;
        String[] b = bl;
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    // 将时间转换成2012年08月12日 这种形式的字符串
    public static String getDateToString3(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time) * 1000);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            StringBuffer sb = new StringBuffer();
            sb.append(year).append("年");
            sb.append(month).append("月").append(day).append("日");
            return sb.toString();
        } catch (NumberFormatException e) {
            return time;
        }

    }

    // 将时间转换成2012-08-12 这种形式的字符串
    public static String getDateToString5(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(time) * 1000);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuffer sb = new StringBuffer();
        sb.append(year).append("-");
        if (month >= 10) {
            sb.append(month).append("-");
        } else {
            sb.append("0").append(month).append("-");
        }
        if (day >= 10) {
            sb.append(day);
        } else {
            sb.append("0").append(day);
        }
        return sb.toString();
    }

    // 将时间转换成2012.08.12 这种形式的字符串
    public static String getDateToString6(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(time) * 1000);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuffer sb = new StringBuffer();
        sb.append(year).append(".");
        if (month >= 10) {
            sb.append(month).append(".");
        } else {
            sb.append("0").append(month).append(".");
        }
        if (day >= 10) {
            sb.append(day);
        } else {
            sb.append("0").append(day);
        }
        return sb.toString();
    }

    public static String getDateToString10(long timeValue) {
        long hour, min, second;
        hour = timeValue / (60 * 60);
        min = timeValue / 60 - hour * 60;
        second = timeValue - min * 60 - hour * 60 * 60;
        StringBuffer sb = new StringBuffer();
        sb.append(hour);
        sb.append(":");
        sb.append(min);
        sb.append(":");
        sb.append(second);
        return sb.toString();
    }


    // 将时间转换成2012－08－12 08:01 这种形式的字符串
    public static String getDateToString4(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(time) * 1000);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        StringBuffer sb = new StringBuffer();
        sb.append(year).append("-");
        if (month >= 10) {
            sb.append(month).append("-");
        } else {
            sb.append("0").append(month).append("-");
        }
        if (day >= 10) {
            sb.append(day);
        } else {
            sb.append("0").append(day);
        }
        sb.append(" ");
        if (hour >= 10) {
            sb.append(hour).append(":");
        } else {
            sb.append("0").append(hour).append(":");
        }
        if (min >= 10) {
            sb.append(min);
        } else {
            sb.append("0").append(min);
        }
        return sb.toString();
    }

    /* 时间戳转换成字符窜 */
    public static String getTimeDiff(String time) {
        StringBuilder sb = new StringBuilder();
        long mss = System.currentTimeMillis() - Long.parseLong(time) * 1000;
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (days > 0) {
            sb.append(days + "天");
        }
        if (hours > 0) {
            sb.append(hours + "小时");
        }
        if (minutes > 0) {
            sb.append(minutes + "分钟");
        }
        if (seconds > 0) {
            sb.append(seconds + "秒");
        }
        sb.append("前");
        return sb.toString();
    }

    // 几分钟前，几小时前，昨天，几天前，一年前，仿照微信的展现形式
    public static String getTimeLineTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        long mss = System.currentTimeMillis() - Long.parseLong(time) * 1000;
        long oneMinute = 60 * 1000;
        long oneHour = oneMinute * 60;
        Calendar calendar = Calendar.getInstance();
        int intHour = calendar.get(Calendar.HOUR_OF_DAY);
        int intMinute = calendar.get(Calendar.MINUTE);
        int intSec = calendar.get(Calendar.SECOND);
        long yesterday = intHour * oneHour + intMinute * oneMinute + intSec * 1000;
        long oneDay = oneHour * 24;
        long oneYear = oneDay * 365;
        if (mss < oneMinute) {
            sb.append("1分钟前");
        } else if (mss < oneHour) {
            sb.append(mss / oneMinute).append("分钟前");
        } else if (mss < yesterday) {
            sb.append(mss / oneHour).append("小时前");
        } else if ((mss - yesterday) < oneDay) {
            sb.append("昨天");
        } else if ((mss - yesterday) < oneYear) {
            sb.append((mss - yesterday) / oneDay).append("天前");
        } else {
            sb.append((mss - yesterday) / oneYear).append("年前");
        }
        return sb.toString();
    }

    // 我的消息界面时间的展现形式
    public static String getMessageTime(String time) {
        StringBuilder sb = new StringBuilder();
        long mss = System.currentTimeMillis() - Long.parseLong(time);
        long oneMinute = 60 * 1000;
        long oneHour = oneMinute * 60;
        long oneDay = 60 * 60 * 24 * 1000;
        long oneYear = oneDay * 365;
        Calendar calendar = Calendar.getInstance();
        int intHour = calendar.get(Calendar.HOUR_OF_DAY);
        int intMinute = calendar.get(Calendar.MINUTE);
        int intSec = calendar.get(Calendar.SECOND);
        long yesterday = intHour * oneHour + intMinute * oneMinute + intSec * 1000;
        calendar.setTimeInMillis(Long.parseLong(time));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (mss < yesterday) {
            sb.append("今天 ");
        } else if ((mss - yesterday) < oneDay) {
            sb.append("昨天 ");
        } else if ((mss - yesterday) < oneYear) {
            sb.append(month);
            sb.append("月").append(day).append("日 ");
        } else {
            sb.append(year).append("年").append(month).append("月").append(day).append("日 ");
        }
        if (hour < 10) {
            sb.append("0").append(hour);
        } else {
            sb.append(hour);
        }
        sb.append(":");
        if (minute < 10) {
            sb.append("0").append(minute);
        } else {
            sb.append(minute);
        }
        return sb.toString();
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToString2(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("yy-MM-dd");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToStringLongInMsgCenter(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
            SimpleDateFormat sftoday = new SimpleDateFormat("HH:mm");

            DateFormat sdf_d_d = new SimpleDateFormat("yyyyMMdd");

            Date nowDate = new Date(System.currentTimeMillis());

            int old = Integer.valueOf(sdf_d_d.format(d));
            int now = Integer.valueOf(sdf_d_d.format(nowDate));

            if (old < now) {
                return sf.format(d);
            } else {
                return "今天" + sftoday.format(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToStringLong(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /* 时间戳转换成字符窜 */
    public static String getDateToStringOrder(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToStringLong2(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /* 时间戳转换成字符窜 */
    public static String getDateToStringLong(long time) {
        try {
            Date d = new Date(time * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToStringLongNot1000(long time) {
        try {
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 时间戳转换成字符窜 */
    public static String getDateMDHI(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 时间戳转换成字符窜 返回MM-DD格式
     */

    public static String getSimpleDateToString(String time) {
        try {
            Date d = new Date(Long.parseLong(time) * 1000);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sf.format(d);
            if (s != null) {
                s = s.substring(s.indexOf("-") + 1);
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保留小数点后两位
     *
     * @param price
     * @return
     */
    public static String getPriceString(String price) {
        try {
            double d = Double.parseDouble(price);
            DecimalFormat decimal = new DecimalFormat("#0.00");
            String str = decimal.format(d);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return price;
        }
    }

    public static boolean isNullOrEmpty(Object s) {
        return s == null || "".equals(s.toString()) || s.toString().length() == 0 || "null".equals(s.toString());
    }

    /**
     * 验证是否为邮箱格式
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    // 判断手机格式是否正确
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("[\\+\\-\\d ]{11,32}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // 判断手机格式是否正确
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(170))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static Intent getPhoneIntent(String phone) {
        Intent phoneIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone));
        // 启动
        return phoneIntent;
    }


    public static String getPriceWithoutPoint(String beforePrice) {
        String price = null;
        if (TextUtils.isEmpty(beforePrice)) {
            return "";
        }
        String[] priceArray = beforePrice.split("\\.");
        if (priceArray != null && priceArray.length == 2) {
            price = priceArray[0];
        } else {
            price = beforePrice;
        }
        return price;
    }

    public static String decodeString(String str) {
        return decodeString(str, "UTF-8");
    }

    public static String encodeString(String str) {
        return encodeString(str, "UTF-8");
    }

    public static String decodeString(String str, String encoding) {
        try {
            return URLDecoder.decode(str, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    public static String encodeString(String str, String encoding) {
        try {
            return URLEncoder.encode(str, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }


    public static String decodeURI(String str) {
        try {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }


    public static Spanned getHtmlStr(String keyText, String valueText) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<font color = " + Color.parseColor("#666666") + ">" + keyText + "</font>");
        stringBuffer.append("<font color = " + Color.parseColor("#ff6666") + ">" + valueText + "</font>");
        return Html.fromHtml(stringBuffer.toString());
    }

    public static String safe(String str) {
        if (TextUtils.isEmpty(str)||"null".equals(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 格式化手机号, 或者投递袋编号
     *
     * @param editable
     * @param segs     在指定的位置插入空格 例如:123 45 6789,传入参数 3,5 返回123 45 6789<br>
     */
    public static void format(Editable editable, int... segs) {
        String oldStr = editable.toString();
        //
        StringBuilder newStr = new StringBuilder(oldStr.replace(" ", ""));
        // 从后面开始插入
        for (int i = segs.length - 1; i >= 0; i--) {
            // 要插入空格的位置
            int position = segs[i];
            if (newStr.length() > position) {
                newStr.insert(position, ' ');
            }
        }
        // 如果oldStr被修改了
        if (newStr.toString().equals(editable.toString()) == false) {
            // editable改变以后会继续回调 #afterTextChanged
            editable.replace(0, editable.length(), newStr.toString());
        }
    }

    /**
     * @param str 要去空格的字符串
     * @return
     */
    public static String strClearEmpty(String str) {
        if (str == null) {
            return "";
        }
        str = str.replace(" ", "");
        return str;
    }
    public static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }
    public static String asciiToString(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
}
