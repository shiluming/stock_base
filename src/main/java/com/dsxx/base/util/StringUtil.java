package com.dsxx.base.util;

import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringUtil {
	
	/**
	 * string类型转换为int型
	 * @author wangzewen
	 * @param str
	 * @return
	 */
	public static int strToInt(String str){
		int param = 0;
		try {
			param = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return param;
	}

	/**
	 * @Description: 将string类型转换为list
	 * @author wangzewen
	 * 2017年7月4日
	 * @param str 格式：xxxxx,xxxxx,xxxx
	 */
	public static List<String> analyzeParams(String str){
		List<String> params = new ArrayList<String>();
		if(StringUtils.isBlank(str)){
			return params;
		}
		if(str.endsWith(",")){
			str = str.substring(0, str.length()-1);
		}
		if(str.startsWith(",")){
			str = str.substring(1,str.length());
		}
		
		if(str.contains(",")){
			String[] args = str.split(",");
			for(int i =0; i<args.length; i++){
				params.add(args[i]);
			}
		}else{
			params.add(str);
		}
		
		return params;
	}
	
	/**
	 * @Description: 将string类型数据转换为BigDecimal类型
	 * @author wangzewen
	 * 2017年7月7日
	 * @param str
	 * @return
	 * BigDecimal
	 */
	public static BigDecimal strToBigDecimal(String str){
		BigDecimal param = null;
		try {
			param = new BigDecimal(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return param;
	}
	
	/**
	 * 按照分割符截取字符串的前后分隔符
	 * @author liuyang
	 * @date 2017年12月27日下午5:40:40
	 * @param str
	 * @param separater
	 * @return
	 */
	public static String trimStr(String str,String separater){
		if(StringUtils.isBlank(str)){
			return "";
		}else{
			if(str.startsWith(separater)){
				str = str.substring(1, str.length());
			}
			if(str.endsWith(separater)){
				str = str.substring(0,str.length()-1);
			}
			return str;
		}
	}

	/**
	 * 功能描述: 小数转百分数<br>
	 * @Author: wzw
	 * @Date: 2018/5/16 17:57
	 * @param: paramOne
	 * @param: paramTwo
	 * @param: doubleFormate 两数相除后的结果格式，如：0.0000
	 * @param: decimalBit 百分数保留的小数位
	 * @return:
	 */
	public static String doubleToPercent(Double paramOne, Double paramTwo, String doubleFormate, int decimalBit){
		//获取两数相除结果
		if(paramOne == null || paramOne == 0){
			return "0%";
		}
		double param = (double)paramOne/paramTwo;
		DecimalFormat df = new DecimalFormat(doubleFormate);
		String decimalResult = df.format(param);

		//获取百分数形式
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(decimalBit);
		String result = nt.format(Double.valueOf(decimalResult));
		return result;
	}


    /**
     * 将 1,2,34,6 这种字符串转换成List<Integer>集合
     * @param numbersStr
     * @param separator
     * @param tClass
     * @param <T>
     * @return
     * @author slm
     * @date 2018/05/21
     */
	public static <T extends Number> List<T> numbersStrToList(String numbersStr, String separator, @NotNull Class<T> tClass){
        if (StringUtils.isBlank(separator)){
            throw new RuntimeException("参数错误");
        }

        List<String> numbersStrList = Splitter.on(separator).omitEmptyStrings()
                .trimResults().splitToList(numbersStr);
        List resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(numbersStrList)){
            for (String s : numbersStrList) {
                if (tClass.equals(Integer.class)){
                    resultList.add(NumberUtils.toInt(s));
                }else if (tClass.equals(Double.class)){
                    resultList.add(NumberUtils.toDouble(s));
                }else if (tClass.equals(Long.class)){
                    resultList.add(NumberUtils.toLong(s));
                }
            }
        }
        return resultList;
    }

	/**
	 * 功能描述: 获取指定范围内的整数（包含起始值和结束值） <br>
	 * @Author: wzw
	 * @Date: 2018/6/29 16:22
	 * @param start 开始值
	 * @param end 结束值
	 */
    public static int  getRandInt(int start, int end){
    	if(start == 0){
    		end += 1;
		}
		Random random = new Random();
		try {
			return random.nextInt(end)+start;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return start;
	}
}
