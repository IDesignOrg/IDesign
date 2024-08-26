package com.my.interrior.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfig {

	@Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolvers(templateResolvers());
        return templateEngine;
    }

    private Set<ITemplateResolver> templateResolvers() {
        Set<ITemplateResolver> resolvers = new HashSet<>();
        resolvers.add(resourceTemplateResolver());
        resolvers.add(fileTemplateResolver());
        return resolvers;
    }

    @Bean
    public ITemplateResolver resourceTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setOrder(1);  // 우선순위 설정
        resolver.setCheckExistence(true); // 템플릿이 실제로 존재하는지 확인
        return resolver;
    }

    @Bean
    public ITemplateResolver fileTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        //나중에 배포할 때 dist로 변경하기
        resolver.setPrefix("classpath:/dist/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setOrder(2);  // 두 번째 우선순위
        resolver.setCheckExistence(true); // 템플릿이 실제로 존재하는지 확인
        return resolver;
    }
}
