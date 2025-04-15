package guru.qa.rococo.config;

import guru.qa.rococo.config.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
@Profile({"local", "docker"})
public class SecurityConfigLocal {

    private final CorsCustomizer corsCustomizer;

    @Autowired
    public SecurityConfigLocal(CorsCustomizer corsCustomizer) {
        this.corsCustomizer = corsCustomizer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer ->
                        customizer.requestMatchers(
                                        antMatcher("/api/session"),
                                        antMatcher("/swagger-ui/**"),
                                        antMatcher("/v3/api-docs/**"),
                                        antMatcher("/actuator/health"),
                                        antMatcher(HttpMethod.GET, "/api/country/**"),
                                        antMatcher(HttpMethod.GET, "/api/artist/**"),
                                        antMatcher(HttpMethod.GET, "/api/museum/**"),
                                        antMatcher(HttpMethod.GET, "/api/painting/**"))
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                ).oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
