package com.my.interrior.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

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
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorize -> authorize.anyRequest().permitAll())
				.cors(cors -> cors.disable())
				.csrf(csrf -> csrf.disable()) // 테스트를 위해 CSRF 보호 비활성화
				.formLogin(formLogin -> formLogin.disable())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
				.oauth2Login(oauth2 -> oauth2
						.redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
						.userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
						.successHandler((request, response, authentication) -> {
							response.sendRedirect("/");
						})
				)
				.sessionManagement(session -> session
						.sessionFixation().newSession()
						.maximumSessions(1)
						.sessionRegistry(sessionRegistry()) // 세션 레지스트리 추가
						.expiredUrl("/signin?expired")
				)
		;

		return http.build();
	}
}