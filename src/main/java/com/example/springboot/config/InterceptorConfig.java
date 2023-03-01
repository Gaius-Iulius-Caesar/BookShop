package com.example.springboot.config;

import com.example.springboot.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 需要在身份下进行的交互就需要进行拦截
                // 拦截book、inventory需调整，因为顾客身份也可能使用
                .addPathPatterns("/user/**", "/inventory/**", "/book/**", "/selldata/**","/store/**","/admin/**","/echarts/**")  // 拦截特定请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns("/**/login", "/**/register", "/**/export/**", "/**/import", "/file/**",
                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/api", "/api-docs", "/api-docs/**")
                .excludePathPatterns("/**/*.html", "/**/*.js", "/**/*.css", "/**/*.woff", "/**/*.ttf");  // 放行静态文件

    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}
