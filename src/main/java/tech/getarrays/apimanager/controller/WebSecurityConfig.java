package tech.getarrays.apimanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import tech.getarrays.apimanager.jwt.AuthEntryPointJwt;
import tech.getarrays.apimanager.jwt.AuthTokenFilter;
import tech.getarrays.apimanager.model.Roles;
import tech.getarrays.apimanager.service.UserDetailsServiceImpl;

import javax.servlet.Filter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("user-photos", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }


    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint((AuthenticationEntryPoint) unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/auth/**","/api/verify-user/**","/api/reset-password","/api/forgot-password","/api/product/**","/api/products/**","/api/payment/charge").permitAll()
                .antMatchers("/api/auth/add-user","/api/order/**").hasAnyRole("ADMIN","MODERATOR","USER","MASTER").antMatchers("/api/product/add").hasAnyRole("ADMIN","MODERATOR").antMatchers("/upload","/files","/files/{id}").permitAll()
                .antMatchers("/api/cart/**").permitAll()
                .antMatchers("/api/product/update").hasAnyRole("ADMIN","MODERATOR")
                .antMatchers("/api/delete/**").hasAnyRole("ADMIN","MODERATOR","MASTER").
                antMatchers("/api/master","/api/auth/add-user").hasRole("MASTER").
                antMatchers("/api/admin").hasRole("ADMIN").
                antMatchers("/api/role/**").hasAnyRole("MASTER","ADMIN").
              anyRequest().authenticated();
        http.addFilterBefore((Filter) authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
