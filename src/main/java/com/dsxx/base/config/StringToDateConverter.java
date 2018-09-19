package com.dsxx.base.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Controller参数日期转换器 将毫秒值转换为Date对象
 *
 * @author slm
 * @date 2018/5/22
 */
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        if (StringUtils.isNotBlank(s)){
            return new Date(Long.valueOf(s));
        }
        return null;
    }
}
