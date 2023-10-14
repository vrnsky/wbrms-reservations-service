package my.edu.sunway.wbrms.wbrmsreservationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
public class CorsConfig {

    private String allowedOriginPattern = "*";
    private String allowedOrigin = "*";
    private String allowedHeader = "*";
    private String allowedMethod = "*";
    private String corsPattern = "/**";

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Collections.singletonList(allowedOriginPattern));
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedHeader(allowedHeader);
        config.addAllowedMethod(allowedMethod);
        source.registerCorsConfiguration(corsPattern, config);
        return new CorsFilter(source);
    }

}
