package com.xzf.config;

import com.xzf.config.security.JwtAuthenticationTokenFilter;
import com.xzf.config.security.RestAuthorizationEntryPoint;
//import com.xzf.config.security.RestfulAccessDeniedHandler;
import com.xzf.config.security.RestfulAccessDeniedHandler;
import com.xzf.entity.User;
import com.xzf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security配置类
 *
 * @author 呵呵厉害了
 * @date 2021/06/22 19:30
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/swagger-ui.html",
                "/captcha",
                "/ws/**"



        );


    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        使用jwt，无需csrf
        http.csrf().disable()
                //基于token，无需session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/login","/logout")
//                .permitAll()
                .anyRequest()
                .authenticated()
                //动态权限配置
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setAccessDecisionManager(customUrlDecisionManager);
//                        o.setSecurityMetadataSource(customFilter);
//                        return o;
//                    }
//                })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();
        //添加jwt过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
//        //添加自定义未授权和未登陆结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user= userService.getUserByUsername(username);
//            if (user != null) {
//                admin.setRoles(adminService.getRoles(admin.getId()));
//                return admin;
//            }
            if(user!=null){
                return user;
            }
            return null;
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
