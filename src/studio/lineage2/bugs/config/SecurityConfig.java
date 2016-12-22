package studio.lineage2.bugs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import studio.lineage2.bugs.recaptcha.RecaptchaAuthenticationFilter;
import studio.lineage2.bugs.recaptcha.RecaptchaFailureRedirectStrategy;
import studio.lineage2.bugs.service.MAccountService;

import java.lang.reflect.Field;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

/**
 Eanseen
 27.05.2016
 */
@Configuration @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired private MAccountService mAccountService;

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.
				csrf().disable().
				authorizeRequests().
				antMatchers("/**/semantic/dist/**", "/**/js/**").permitAll().
				antMatchers("/", "/bugs/**", "/api/**", "/enter/**").permitAll().
				antMatchers("/admin/**").hasRole("ADMIN").
				anyRequest().hasRole("USER").and().
				logout().logoutUrl("/enter/logout");

		wrap(http.formLogin()).loginPage("/enter/auth").and().logout().permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(mAccountService::findByUsername).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public RecaptchaAuthenticationFilter authenticationFilter() throws Exception
	{
		RecaptchaAuthenticationFilter filter = new RecaptchaAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() throws Exception
	{
		SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
		handler.setUseForward(false);
		handler.setDefaultFailureUrl("/enter/auth");
		handler.setRedirectStrategy(new RecaptchaFailureRedirectStrategy());
		return handler;
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() throws Exception
	{
		SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
		handler.setRedirectStrategy((request, response, url)->response.sendRedirect("/admin"));
		return handler;
	}

	private FormLoginConfigurer<HttpSecurity> wrap(FormLoginConfigurer<HttpSecurity> loginConfigurer) throws Exception
	{
		Field authFilterField = findField(loginConfigurer.getClass(), "authFilter", AbstractAuthenticationProcessingFilter.class);
		makeAccessible(authFilterField);
		setField(authFilterField, loginConfigurer, authenticationFilter());
		return loginConfigurer.failureUrl("/enter/auth").failureHandler(authenticationFailureHandler()).successHandler(successHandler());
	}
}