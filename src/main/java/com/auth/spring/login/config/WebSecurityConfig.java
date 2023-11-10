package com.auth.spring.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.spring.login.security.jwt.AuthEntryPointJwt;
import com.auth.spring.login.security.jwt.AuthTokenFilter;
import com.auth.spring.login.security.services.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(AUTH_WHITE_LIST).permitAll()
            .anyRequest().authenticated());
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("eyuel98")
        .password("string")
        .roles("USER", "ADMIN", "MODERATE")
        .build();
    return new InMemoryUserDetailsManager(user);
  }

//   @Bean
//   public OpenAPI openAPI() {
//     return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Cookie"))
//         .components(new Components().addSecuritySchemes("Bearer Cookie", createAPIKeyScheme()))
//         .info(new Info().title("My REST API")
//             .description("Some custom description of API.")
//             .version("1.0").contact(new Contact().name("Eyuel Belete")
//                 .email("www.allora-auth.com").url("eyuelbelete98@gmail.com"))
//             .license(new License().name("License of API")
//                 .url("API license URL")));
//   }

//   private SecurityScheme createAPIKeyScheme() {
//     return new SecurityScheme()
//         .type(SecurityScheme.Type.APIKEY)
//             .in(SecuritySchemeIn.COOKIE) // Use the correct import
//             .bearerFormat("JWT")
//             .scheme("bearer");
// }

  private static final String[] AUTH_WHITE_LIST = {
      "/api/auth/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/v3/api-docs/**",
      "/actuator/*",
      "/swagger-ui/**",
      "/favicon.ico",
      "/swagger-ui/index.html"

  };
}
