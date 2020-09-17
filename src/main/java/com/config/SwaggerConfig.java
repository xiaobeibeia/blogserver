package com.config;

import com.github.xiaoymin.knife4j.spring.annotations.*;
import com.utils.constants.JwtConstants;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import springfox.bean.validators.configuration.*;
import springfox.documentation.builders.*;
import springfox.documentation.schema.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

import java.util.*;

/**
 * @author lijing
 * @e-mail 1413979079@qq.com
 * @date 2020-06-28 22:43
 * @description Swagger 配置类
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Bean
    public Docket docket(Environment environment) {
        // 如果在dev环境（开发环境）就开启Swagger
        boolean isDev = environment.acceptsProfiles(Profiles.of("dev"));
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        // 调试中可以修改请求头
        ticketPar.name(JwtConstants.TOKEN_HEADER)
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(ticketPar.build());

            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .groupName("默认接口")
                    .enable(isDev)
                    .select()
                    //指定要扫描的包的路径
                    .apis(RequestHandlerSelectors.basePackage("com.controller"))
                    .paths(PathSelectors.any())
                    .build();
        }

        /**
         * 配置Swagger的ApiInfo
         *
         * @return API配置信息
         */
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("SpringBoot项目 后台服务API接口文档")
                    .description("使用 knife4j 搭建的后台服务API接口文档")
                    .termsOfServiceUrl("http://localhost:9000/")
                    .version("1.0.0")
                    .build();
        }

}

