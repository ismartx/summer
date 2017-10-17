package org.smartx.summer.config;

import org.smartx.summer.session.SessionAndTokenConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;


/**
 * Created by Ming on 2016/11/1. Swagger 配置，官网：https://springfox.github.io/springfox/docs/current/
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean isEnable;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(isEnable)
                .globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name("Request-Id")
                                .description("Request-Id")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()))
                .globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name("X-User-Agent")
                                .description("X-User-Agent")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .defaultValue("ver=11;caller=app;ch=11;platform=Android;mac=XXX")
                                .build()))
                .globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name(SessionAndTokenConstants.AUTHORIZATION_HEADER)
                                .description(SessionAndTokenConstants.AUTHORIZATION_HEADER)
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()))
                .globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name(HttpHeaders.IF_NONE_MATCH)
                                .description(HttpHeaders.IF_NONE_MATCH)
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()))
                ;
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("Summer 测试用例文档",
                "1、请求时请带上 Request-Id  和 X-User-Agent;" +
                        "2、带权限接口需要带上 Authorization" +
                        "3、支持缓存的接口需要带上 If-None-Match",

                "version 0.1",
                "smartx team",
                new Contact("summer@qq.com", "url", "String email"),
                "The Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
        );
        return apiInfo;
    }
}
