package com.activiti7.activiti7imoocdevelop.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Knife4jProperties
 *
 * @author 多宝
 * @since 2022/7/24 19:29
 */
@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    @SuppressWarnings("SpellCheckingInspection,unused")
    public Docket createRestApiDoc() {
        //schema
        List<GrantType> grantTypes = new ArrayList<>();
        // 密码模式
        String passwordTokenUrl = "http://localhost:8080/oauth/token";

        ResourceOwnerPasswordCredentialsGrant
                credentialsGrant =
                new ResourceOwnerPasswordCredentialsGrant(passwordTokenUrl);
        grantTypes.add(credentialsGrant);
        OAuth oAuth = new OAuthBuilder().name("oauth2")
                .grantTypes(grantTypes).build();
        //context
        //scope方位
        List<AuthorizationScope> scopes = new ArrayList<>();
        scopes.add(new AuthorizationScope("read", "read  resources"));
        scopes.add(new AuthorizationScope("write", "write resources"));
        scopes.add(new AuthorizationScope("reads", "read all resources"));
        scopes.add(new AuthorizationScope("writes", "write all resources"));

        SecurityReference securityReference = new SecurityReference("oauth2",
                                                                    scopes.toArray(
                                                                            new AuthorizationScope[]{}));
        SecurityContext securityContext =
                new SecurityContext(Lists.newArrayList(securityReference),
                                    PathSelectors.ant("/api/**"), null, null);
        // schemas
        List<SecurityScheme> securitySchemes = Lists.newArrayList(oAuth);
        // securityContext
        List<SecurityContext> securityContexts =
                Lists.newArrayList(securityContext);

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                              .basePackage(
                                      "com.activiti7.activiti7imoocdevelop.controller"))
                .paths(PathSelectors.any())
                .build()
                /* .securitySchemes(securitySchemes)
                .securityContexts(securityContexts) */;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().description(
                        "Activiti7 study project rest api")
                .termsOfServiceUrl("localhost:8080/doc.html")
                .contact(new Contact("duobao",
                                     "localhost:8080/doc.html",
                                     "y908063221@gmail.com"))
                .version("v1.0")
                .build();
    }

}
