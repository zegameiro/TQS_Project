package deti.tqs.backend.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Generated
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  
  @Bean
  CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration conf = new CorsConfiguration();

    conf.setAllowedOrigins(List.of("*"));
    conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    conf.setAllowedHeaders(
      List.of("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization")
    );
    conf.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", conf);

    return source;

  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.GET, "/api/facility/*").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/facility/admin/*").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/facility/admin/*").permitAll()
        .requestMatchers(HttpMethod.GET, "/docs").permitAll()
        .requestMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
        .requestMatchers(HttpMethod.GET, "/v3/api-docs/*").permitAll()
        .anyRequest().authenticated())
      .build();
  }

}
