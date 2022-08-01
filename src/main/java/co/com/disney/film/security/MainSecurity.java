package co.com.disney.film.security;

import co.com.disney.film.security.jwt.JwtEntryPoint;
import co.com.disney.film.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurity {

    private final JwtEntryPoint jwtEntryPoint;

    public MainSecurity(JwtEntryPoint jwtEntryPoint) {
        this.jwtEntryPoint = jwtEntryPoint;
    }

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUIPath;

    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;

    private String key = "YRv13MrZah/rHJPMGIN6AjdjB09F9gpIC7i9mdFwdIDZ296doUg/nhG/mQ/CnlxPNtcWR6z6RCKtW5cCspGM9w==";

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors()
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/auth/**")//lets requests without auth at endpoint auth
            .permitAll()
            //openapi3 swagger docs
            .antMatchers (swaggerUIPath.substring (0, swaggerUIPath.lastIndexOf ("/") + 1) + "**")
            .permitAll ()
            .antMatchers (apiDocsPath + "/**")
            .permitAll ()
            .antMatchers (apiDocsPath.substring (0, apiDocsPath.lastIndexOf ("/") + 1) + "api-docs.yaml")
            .permitAll ()

            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
