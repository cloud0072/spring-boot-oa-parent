package com.github.cloud0072.base.config;

import com.github.cloud0072.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName: WebSecurityConfig
 * @Description: TODO
 * @author caolei
 * @date 2018/9/27 14:55
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //定义不需要认证就可以访问
                .antMatchers("/assets").permitAll()
                //定义哪些url需要保护
                .antMatchers().authenticated()
                .and()
                .formLogin()
                .loginPage("/prepare_login").permitAll()
                .successForwardUrl("/index")
                .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }


}
