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
    
    String apiFacilityAdmin = "/api/facility/admin/*";
    String apiRoomAdmin = "/api/room/admin/*";
    String apiChairAdmin = "/api/chair/admin/*";

    return httpSecurity
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.GET, "/api/facility/*").permitAll()
        .requestMatchers(HttpMethod.POST, apiFacilityAdmin).permitAll()
        .requestMatchers(HttpMethod.PUT, apiFacilityAdmin).permitAll()
        .requestMatchers(HttpMethod.DELETE, apiFacilityAdmin).permitAll()
        .requestMatchers(HttpMethod.GET, "/api/room/*").permitAll()
        .requestMatchers(HttpMethod.POST, apiRoomAdmin).permitAll()
        .requestMatchers(HttpMethod.PUT, apiRoomAdmin).permitAll()
        .requestMatchers(HttpMethod.DELETE, apiRoomAdmin).permitAll()
        .requestMatchers(HttpMethod.GET, "/api/chair/*").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/chair/room/*").permitAll()
        .requestMatchers(HttpMethod.POST, apiChairAdmin).permitAll()
        .requestMatchers(HttpMethod.PUT, apiChairAdmin).permitAll()
        .requestMatchers(HttpMethod.DELETE, apiChairAdmin).permitAll()
        .requestMatchers(HttpMethod.GET, "/api/reservation/*").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/reservation/*").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/speciality/*").permitAll()
        .requestMatchers(HttpMethod.GET, "/docs").permitAll()
        .requestMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
        .requestMatchers(HttpMethod.GET, "/v3/api-docs/*").permitAll()
        .anyRequest().authenticated())
      .build();
  }

}
