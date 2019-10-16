package com.dyd.sisbr.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	  auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(NoOpPasswordEncoder.getInstance())
		.usersByUsernameQuery(
			"select username, password, enabled from usuario where username=?")
		.authoritiesByUsernameQuery(
			"select pu.username, p.nombre from perfil p inner join perfil_usuario pu on p.idPerfil = pu.idPerfil where pu.username = ?");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/webjars/**", "/js/**", "/img/**", "/css/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/inicio")
//			.failureUrl("/login?error")
			.usernameParameter("username")
			.passwordParameter("password")
			.permitAll()
			.and()
		.logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/login?logout")
	        .permitAll()
	        .and()
	    .exceptionHandling()
	    	.accessDeniedPage("/403")
	    	.and()
		.csrf();
//		.csrf().disable();
	}
}
