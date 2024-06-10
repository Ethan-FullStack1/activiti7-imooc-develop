package com.activiti7.activiti7imoocdevelop.config;

import com.activiti7.activiti7imoocdevelop.util.GlobalConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * web相关配置
 *
 * @author 多宝
 * @since 2022/7/24 20:33
 */
@Configuration
public class MyWebAppConfig implements WebMvcConfigurer {

    /**
     * 放行Swagger相关的所有配置
     *
     * @param registry 注册资源配置
     * @author 多宝
     * @since 2022/7/24 21:53
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger-resources/**")
                .addResourceLocations("classpath:/");


        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/resources/");//默认也有这个路径映射
        registry.addResourceHandler("bpmn/**").addResourceLocations(
                GlobalConfig.BPMN_PathMapping);

    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }

    /**
     * 解决数据返回浏览器中文乱码
     *
     * @return org.springframework.http.converter.HttpMessageConverter<java.lang.String>
     * @author 多宝
     * @since 2022/7/24 22:44
     */
    @Bean
    public HttpMessageConverter<String> converter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }
}
