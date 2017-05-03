package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.profile.client.config.ProfileClientConfig;
import com.transformuk.hee.tis.security.JwtAuthenticationEntryPoint;
import com.transformuk.hee.tis.security.JwtAuthenticationProvider;
import com.transformuk.hee.tis.security.JwtAuthenticationSuccessHandler;
import com.transformuk.hee.tis.security.RestAccessDeniedHandler;
import com.transformuk.hee.tis.security.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(ProfileClientConfig.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	@Autowired
	private RestAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private JwtAuthenticationProvider authenticationProvider;

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return new ProviderManager(Arrays.asList(authenticationProvider));
	}
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter("/api/**");
		authenticationTokenFilter.setAuthenticationManager(authenticationManager());
		authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
		return authenticationTokenFilter;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// we don't need CSRF because our token is invulnerable
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).accessDeniedHandler(accessDeniedHandler)
				.and()
				 // don't create session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and()
		// Custom JWT based security filter
		httpSecurity
				.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}
}
