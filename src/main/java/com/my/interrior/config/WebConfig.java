package com.my.interrior.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
//스프링부트를 쓰면 그레이들에 웹이라는 애 작성하면 알아서 찾는 애 알아서 설정해준다
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
    //클래스 주입 받았으니 알아서 생성자 디폴트 뭐 다 했음 오토와이어드로 주입받았으니 인터셉터 클레스로 넘어가
    //인터셉터 안에 있는애들을 수행한다

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error/**","/auth/**","/","/login", "/board/**", "/static/**", "/css/**", "/js/**", "/include/**", "/image/**", "/logout", "/board/faq/search/**", "/dist/**"); //auth명시된건 패스시킨다
    }
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// 1. Spring Boot의 기본 정적 리소스 경로 (첫 번째 우선순위)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/resources/", "classpath:/META-INF/resources/");

        // 2. 커스터마이징된 정적 리소스 경로 (두 번째 우선순위)
		// `three` 디렉토리 내의 `public` 폴더에서 정적 파일을 서빙
		registry.addResourceHandler("/dist/**")
				.addResourceLocations("file:" + System.getProperty("user.dir") + "/three/dist/");
	}
}
