package com.ataraxia.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author MyPC
 */
@Configuration
@EnableSwagger2
public class Knife4jConfiguration {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("微服务【用户模块】")
                .description("用户模块文档")
                .termsOfServiceUrl("tps://github.com/lilong19970214")
                .contact(new Contact("ataraxia", "https://github.com/lilong19970214", "19970214"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                //.groupName("1.0版本")
                .select()
                //指定controller（接口）扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("com.ataraxia.microservices.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
