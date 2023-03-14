package com.hobam.vote.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig{
	private UserDetailsService userDetailsService;
	
	public SecurityConfig(UserDetailsService userD) {
		this.userDetailsService = userD;
	}
	
	@Bean
	public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public AuthenticationManager authenticationManager(
                                 AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			
//		http.csrf()
//	      .disable()
//	      .authorizeHttpRequests()
//	      .requestMatchers("/admin/**")
//	      .hasAnyRole("ROLE_ADMIN")
//	      .requestMatchers("/user/**")
//	      .hasAnyRole("ROLE_USER", "ROLE_ADMIN", "USER", "ADMIN")
//	      .requestMatchers("/vote/**")
//	      .hasAnyRole("ROLE_USER", "ROLE_ADMIN")
//	      .requestMatchers("/**")
//	      .anonymous()
//	      .anyRequest().permitAll();
//	      .authenticated()
//	      .and()
//	      .httpBasic()
//	      .and()
//	      .sessionManagement()
//	      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
//                        authorize.anyRequest().authenticated()
		                authorize.requestMatchers(HttpMethod.GET, "/**").permitAll()
			                .requestMatchers("/**").permitAll()
			                .anyRequest().authenticated()
//                                .and().formLogin()
//                                .failureHandler((request, response, exception) -> System.out.println(response))
//                                .permitAll()

                );

        return http.build();
    }
	
	
}
