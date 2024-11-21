package by.it_academy.jd2.classifier_service.config;


import by.it_academy.jd2.classifier_service.controller.filter.JWTFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTFilter jwtFilter) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        })
        );

        httpSecurity.authorizeHttpRequests(requests ->
                requests.requestMatchers(HttpMethod.POST,
                                "/classifier/currency").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers(HttpMethod.GET,
                                "/classifier/currency").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/classifier/operation/category").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers(HttpMethod.GET,
                                "/classifier/operation/category").permitAll()
                        .anyRequest().authenticated()

        );

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }


}
