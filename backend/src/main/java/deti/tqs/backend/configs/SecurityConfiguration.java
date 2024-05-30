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

    String apiFacilityadmin = "/api/facility/admin/**";
    String apiRoomadmin = "/api/room/admin/**";

    return httpSecurity
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.GET, "/api/facility/**").permitAll()
        .requestMatchers(HttpMethod.POST, apiFacilityadmin).permitAll()
        .requestMatchers(HttpMethod.PUT, apiFacilityadmin).permitAll()
        .requestMatchers(HttpMethod.DELETE, apiFacilityadmin).permitAll()
        .requestMatchers(HttpMethod.GET, "/api/room/**").permitAll()
        .requestMatchers(HttpMethod.POST, apiRoomadmin).permitAll()
        .requestMatchers(HttpMethod.PUT, apiRoomadmin).permitAll()
        .requestMatchers(HttpMethod.DELETE, apiRoomadmin).permitAll()
        .requestMatchers(HttpMethod.GET, "/api/reservation/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/reservation/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/reservation/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/speciality/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/docs").permitAll()
        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
        .anyRequest().authenticated())
      .build();
  }

}
