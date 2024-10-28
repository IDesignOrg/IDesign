package com.my.interrior.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.my.interrior.interceptor.Interceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Interceptor interceptor;
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/v3/**","/swagger-ui/**","/admin/api/**","/api/**", "/signin/**", "/signup/**", "/forgot-id/**","/forgot-password/**","/error/**","/auth/**","/", "/board/**", "/static/**", "/css/**", "/js/**", "/include/**", "/image/**", "/logout", "/board/faq/search/**", "/dist/**", "/save/**", "/get/**", "/favicon.ico", "/meta/favicon.ico"); //auth명시된건 패스시킨다
    }
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// 1. Spring Boot의 기본 정적 리소스 경로 (첫 번째 우선순위)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/resources/", "classpath:/META-INF/resources/");

        // 2. 커스터마이징된 정적 리소스 경로 (두 번째 우선순위)
		// `three` 디렉토리 내의 `public` 폴더에서 정적 파일을 서빙
		registry.addResourceHandler("/dist/**")
				.addResourceLocations("classpath:/dist/");
		
		registry.addResourceHandler("/public/**")
		.addResourceLocations("classpath:/public/");
	}
    
    //Swagger 배포 사이트 CORS 에러 해결
    //Author : 한민욱
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://idesign.r-e.kr")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
