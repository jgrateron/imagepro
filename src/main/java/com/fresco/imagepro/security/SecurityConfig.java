package com.fresco.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

 	@Override
 	protected void configure(HttpSecurity http) throws Exception 
 	{
 		http
 		 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and().cors().and()
         .csrf().disable()
         .authorizeRequests()
         .antMatchers("/").permitAll()
         .antMatchers("/api/v1/resize").hasRole("USER")
         .antMatchers("/api/v1/rotate").hasRole("USER")
         .antMatchers("/api/v1/crop").hasRole("USER")
         .anyRequest().authenticated().and()
         .httpBasic();
 	}

 	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth.inMemoryAuthentication()
        .withUser("user")
        .password("{noop}123456")
        .roles("USER");
 	}
}
