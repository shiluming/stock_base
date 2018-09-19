package com.dsxx.base.aspect;

import com.dsxx.base.service.exception.ParamErrorException;
import com.dsxx.base.service.exception.RequestExpirationException;
import com.dsxx.base.service.exception.SignErrorException;
import com.dsxx.base.util.DateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;

/**
 * 这个拦截器功能是进行简单的接口签名拦截，目的是防止接口被恶意复用
 *
 *
 * @author slm
 * @date 2018/5/18
 */
@Component
public class SignVerifyInterceptor extends HandlerInterceptorAdapter {

    private static final Logger Log = LoggerFactory.getLogger(SignVerifyInterceptor.class);

    /**
     * 请求签名参数名
     */
    private static final String SIGN_KEY_NAME = "sign";
    /**
     * 请求过期时间 分钟
     */
    private static final long EXPIRATION_TIME = 5*60*1000;

    private static final String PARAM_SEPARATOR = "+";


    /**
     * 接口参数签名验证 目的：防止接口参数随意篡改，同一个参数的请求只能在有效时间内使用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     * @author slm
     * @date 2018/05/18
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取参数中的签名
        String sign = request.getParameter(SIGN_KEY_NAME);
        // 请求的时间戳
        long timestamp = NumberUtils.toLong(request.getParameter("timestamp"));
        if (StringUtils.isBlank(sign) || timestamp <= 0){
            throw new ParamErrorException("参数错误");
        }

        // 判断请求是否在有效时间内 只允许±5分钟内的请求
        if (Math.abs(System.currentTimeMillis() - timestamp) > EXPIRATION_TIME){
            throw new RequestExpirationException("请求过期,请求时间："+DateUtils.dateToString(new Date(timestamp),
                    DateUtils.PATTERN_DATETIME));
        }

        // 获取请求中除了签名参数外的所有参数，按参数名升序排序，然后拼接所有参数值，计算拼接后的字符串的MD5
        TreeMap<String, String> paramsMap = new TreeMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()){
            String paramName = params.nextElement().trim();
            paramsMap.put(paramName,URLDecoder.decode(request.getParameter(paramName), "UTF-8"));
        }
        Log.debug("参数：{}",paramsMap);
        List<String> valueList = new ArrayList<>();
        paramsMap.forEach((key, value) -> {
            if (SIGN_KEY_NAME.equals(key)){
                return;
            }
            valueList.add(value);
        });
        String md5 = DigestUtils.md5Hex(StringUtils.join(valueList,PARAM_SEPARATOR));
        Log.debug("参数计算签名：{}", md5);
        // 验证计算出的md5是否和参数中的sign是否相等
        if (!sign.equalsIgnoreCase(md5)){
            throw new SignErrorException("签名错误");
        }
        return true;
    }
}
