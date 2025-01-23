package com.my.interrior.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final OAuth2UserServiceImplement oAuth2UserService;
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.anyRequest().permitAll())
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .oauth2Login(oauth2 -> oauth2
                		.redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                		.userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                		.successHandler((request, response, authentication) -> {
                		    // 로그인 성공 후 처리 로직
                		    response.sendRedirect("/"); // 홈 페이지로 리다이렉트
                		})
                		)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
