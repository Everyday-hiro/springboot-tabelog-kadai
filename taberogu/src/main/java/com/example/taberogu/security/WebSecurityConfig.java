package com.example.taberogu.security;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFillterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.authorizeHttpRequests((requests) -> requests
		.requestMatchers("/css/**", "/image/**", "/storage/**", "/").permitAll()
		.requestMatchers("/admin/**").
		)
	}
}
