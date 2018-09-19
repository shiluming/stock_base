package com.dsxx.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 api文档配置
 *
 * 访问路径 http://localhost:8099/context-path/swagger-ui.html
 *
 * 使用方法参考： https://blog.csdn.net/forezp/article/details/71023536
 * @author slm
 * @date 2018/4/19
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 根据配置文件判断是否开启Swagger文档
     */
    @Value("${swagger.status:false}")
    private boolean isActive;
    @Value("${swagger.title:来电API文档}")
    private String title;
    @Value("${swagger.description:来电API文档}")
    private String description;
    @Value("${swagger.version:1.0}")
    private String version;

    @Bean
    public Docket createRestApi() {
        if (isActive){
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.dsxx"))
                    .paths(PathSelectors.any())
                    .build();
        }else {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .paths(PathSelectors.none())
                    .build();
        }
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl("http://www.imdsxx.com")
                .version(version)
                .build();
    }

    @Bean
    public UiConfiguration uiConfiguration(){
        return new UiConfiguration(null, "list", "alpha", "model",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, true, true, 60000L);
    }
}
