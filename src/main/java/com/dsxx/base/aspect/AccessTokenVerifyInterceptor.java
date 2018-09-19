package com.dsxx.base.aspect;

import com.dsxx.base.annotaion.NoVerify;
import com.dsxx.base.service.exception.NoLoginException;
import com.dsxx.base.service.exception.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author slm
 * @date 2018/4/20
 */
@Component
public class AccessTokenVerifyInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenVerifyInterceptor.class);

    public static final String TOKEN_NAME = "platform_token";


    @Autowired
    public AccessTokenVerifyInterceptor() {

    }

    /**
     * 请求调用前处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean flag =true;
        // 加上这个注解不校验接口权限
        NoVerify noVerify = ((HandlerMethod) handler).getMethod().getAnnotation(NoVerify.class);
        if (noVerify != null){
            return true;
        }
        String url = request.getRequestURI();

        LOG.debug("校验接口：{} {}");
//        if (!flag){
//            throw new NoPermissionException("没有权限");
//        }
        return flag;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
