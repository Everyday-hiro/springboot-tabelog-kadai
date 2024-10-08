package com.example.taberogu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**",
								"/restaurant", "/restaurant/{id}", "/restaurant/category/{id}",
								"/stripe/webhook", "/password/**", "/error", "/login?error",
								"/create-checkout-session")
						.permitAll() /**全てのユーザーにアクセス許可**/
						.requestMatchers("/admin/**").hasRole("ADMIN")/**管理者にのみ許可**/
						.anyRequest().authenticated())/**上のURL以外はログインが必要（会員または管理者どっちでもOK）**/
				.formLogin((form) -> form
						.loginPage("/login")/**ログインページのURL**/
						.loginProcessingUrl("/login")/**ログインフォームの送信先URL**/
						.defaultSuccessUrl("/?loggedIn")/**ログイン成功時のリダイレクト先URL**/
						.failureUrl("/login?error")/**ログイン失敗時のリダイレクト先URL**/
						.permitAll())
				.logout((logout) -> logout
						.logoutSuccessUrl("/?loggedOut")/**ログアウト時のリダイレクト先URL**/
						.permitAll())
				.csrf().ignoringRequestMatchers("/stripe/webhook");

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
