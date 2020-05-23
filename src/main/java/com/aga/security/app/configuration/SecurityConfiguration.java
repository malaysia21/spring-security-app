package com.aga.security.app.configuration;

import com.aga.security.app.model.UserEntityToUserDetails;
import com.aga.security.app.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private UserEntityToUserDetails userEntityToUserDetails;


    public SecurityConfiguration(UserDetailsService userDetailsService, UserRepository userRepository, UserEntityToUserDetails userEntityToUserDetails) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.userEntityToUserDetails = userEntityToUserDetails;
    }

    @Override
    //define data source
    //roles, permissions
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //In MEMORY AUTHENTICATION
        /*
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("pass"))
                .authorities("ACCESS_ADVERTISEMENT","ACCESS_ADVERTISEMENTS","ROLE_ADMIN")

                .and()
                .withUser("user")
                .password(passwordEncoder().encode("pass"))
                .roles("USER")

                .and()
                .withUser("manager")
                .password(passwordEncoder().encode("pass"))
                .authorities("ACCESS_ADVERTISEMENT", "ROLE_MANAGER");

        //when user has both roles and authorities it has to be combined in authorities list
         */

        //DATABASE AUTHENTICATION PROVIDER ver 2
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        //DATABASE AUTHENTICATION PROVIDER
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    //authorize request for JWT Authentication
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userRepository, userEntityToUserDetails))
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/advertisements").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/api/users").hasRole("ADMIN");

    }

    /*
    @Override
    //authorize request for HTTP Form Based Authentication (custom login page)
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/index").permitAll()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/management/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/api/advertisement").hasAuthority("ACCESS_ADVERTISEMENT")
                .antMatchers("/api/advertisements").hasAuthority("ACCESS_ADVERTISEMENTS")
                .antMatchers("/api/users").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginProcessingUrl("/sign-in")
                .loginPage("/login").permitAll()
                .usernameParameter("txtUsername")
                .passwordParameter("txtPassword")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .and()
                .rememberMe().tokenValiditySeconds(200000).key("myKey").rememberMeParameter("checkRememberMe");
    }
     */


    /*
    @Override
    //authorize request HTTP Basic Authentication (default login window)
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/index").permitAll()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/management/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/api/advertisement").hasAuthority("ACCESS_ADVERTISEMENT")
                .antMatchers("/api/advertisements").hasAuthority("ACCESS_ADVERTISEMENTS")
                .antMatchers("/api/users").hasRole("ADMIN")
                .and()
                .httpBasic();


    }
     */

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


}
