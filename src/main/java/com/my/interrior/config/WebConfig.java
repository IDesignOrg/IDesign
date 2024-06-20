package com.my.interrior.config;

import com.my.interrior.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
//스프링부트를 쓰면 그레이들에 웹이라는 애 작성하면 알아서 찾는 애 알아서 설정해준다
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Interceptor interceptor;
    //클래스 주입 받았으니 알아서 생성자 디폴트 뭐 다 했음 오토와이어드로 주입받았으니 인터셉터 클레스로 넘어가
    //인터셉터 안에 있는애들을 수행한다

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("shshshshsh");
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**","/","/login", "/board/**", "/static/**", "/css/**", "/js/**", "/include/**", "/image/**", "/logout"); //auth명시된건 패스시킨다




    }

}
