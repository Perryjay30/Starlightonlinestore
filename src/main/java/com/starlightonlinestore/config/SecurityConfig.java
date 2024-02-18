package com.starlightonlinestore.config;

import com.starlightonlinestore.config.oauth2.AllOAuth2LoginSuccessHandler;
import com.starlightonlinestore.config.oauth2.CustomOAuth2UserService;
import com.starlightonlinestore.config.oauth2.successhandlers.FacebookOAuth2LoginSuccessHandler;
import com.starlightonlinestore.config.oauth2.successhandlers.GithubOAuth2LoginSuccessHandler;
import com.starlightonlinestore.config.oauth2.successhandlers.GoogleOAuth2LoginSuccessHandler;
import com.starlightonlinestore.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AllOAuth2LoginSuccessHandler allOAuth2LoginSuccessHandler;
    private final GoogleOAuth2LoginSuccessHandler googleOAuth2LoginSuccessHandler;
    private final GithubOAuth2LoginSuccessHandler githubOAuth2LoginSuccessHandler;
    private final FacebookOAuth2LoginSuccessHandler facebookOAuth2LoginSuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, UserRepository userRepository, CustomOAuth2UserService customOAuth2UserService,
                          AllOAuth2LoginSuccessHandler allOAuth2LoginSuccessHandler, GoogleOAuth2LoginSuccessHandler googleOAuth2LoginSuccessHandler, GithubOAuth2LoginSuccessHandler githubOAuth2LoginSuccessHandler, FacebookOAuth2LoginSuccessHandler facebookOAuth2LoginSuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
        this.customOAuth2UserService = customOAuth2UserService;
        this.allOAuth2LoginSuccessHandler = allOAuth2LoginSuccessHandler;
        this.googleOAuth2LoginSuccessHandler = googleOAuth2LoginSuccessHandler;
        this.githubOAuth2LoginSuccessHandler = githubOAuth2LoginSuccessHandler;
        this.facebookOAuth2LoginSuccessHandler = facebookOAuth2LoginSuccessHandler;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new FetchUserDetailsFromDbService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("api/v1/customer/register", "api/v1/vendor/register", "starlight/authenticateUsers", "api/v1/customer/createAccount/{email}",
                        "api/v1/vendor/createAccount/{email}", "api/v1/customer/login", "api/v1/customer/getAllUsers", "api/v1/vendor/vendorLogin", "login/oauth2/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("api/v1/customer/**", "api/v1/vendor/**", "api/v1/product/**", "cartingAndProduct/**")
                .authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(allOAuth2LoginSuccessHandler)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AllOAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new AllOAuth2LoginSuccessHandler(googleOAuth2LoginSuccessHandler, userRepository, githubOAuth2LoginSuccessHandler, facebookOAuth2LoginSuccessHandler);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
