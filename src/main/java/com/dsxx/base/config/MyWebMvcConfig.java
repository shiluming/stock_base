package com.dsxx.base.config;

import com.dsxx.base.aspect.AccessTokenVerifyInterceptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * @author slm
 * @date 2018/4/20
 */
@Configuration
public class MyWebMvcConfig extends WebMvcConfigurerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(MyWebMvcConfig.class);

    @Autowired
    private AccessTokenVerifyInterceptor accessTokenVerifyInterceptor;

    /**
     * 需要拦截的url<br/>
     * 不包括context-path。如 /user/**,/group/**
     */
    @Value("${interceptUrls:/test/**}")
    private String interceptUrls;

    /**
     * 拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        InterceptorRegistration registration = registry.addInterceptor(accessTokenVerifyInterceptor);
        // 只拦截指定的路径前缀
        if (StringUtils.isNotBlank(interceptUrls)){
            String[] interceptUrlList = interceptUrls.split(",");
            if (interceptUrlList.length>0){
                registration.addPathPatterns(interceptUrlList);
                LOG.info("拦截的url:{}",Arrays.toString(interceptUrlList));
            }
        }

        // 不拦截druid监控相关rul
        registration.excludePathPatterns("/druid/**")
                    // 不拦截swagger相关的url
                    .excludePathPatterns("/swagger-ui.html")
                    .excludePathPatterns("/swagger-resources/**")
                    .excludePathPatterns("/webjars/**");

    }
}
