package com.zhiiothub.v1.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket restApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("标准接口")
                .apiInfo(apiInfo("林区IOThub构建的RESTful APIS", "1.0"))
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhiiothub.v1.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(String title, String version){
        return new ApiInfoBuilder()
                .title(title)
                .termsOfServiceUrl("http://localhost/8080")
                .description("林区IOThub接口说明")
                .version(version)
                .build();
    }
}
