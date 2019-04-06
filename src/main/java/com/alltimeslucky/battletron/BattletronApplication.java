package com.alltimeslucky.battletron;

import com.alltimeslucky.battletron.configuration.JerseyResourceConfig;

import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * The Battletron application main class.
 */
@SpringBootApplication
public class BattletronApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattletronApplication.class, args);
    }

    /**
     * Registers a servlet bean with Spring Boot for the Jersey REST API.
     * @return A configured ServletRegistration bean
     */
    @Bean
    public ServletRegistrationBean jerseyApi() {
        ServletRegistrationBean jerseyApi = new ServletRegistrationBean<>(new ServletContainer(new JerseyResourceConfig()));
        jerseyApi.addUrlMappings("/api/*");
        jerseyApi.setName("battletron-api");
        jerseyApi.setLoadOnStartup(0);
        return jerseyApi;
    }

    /**
     * Registers a Filter bean with Spring Boot for allowing requests from the Angular client when running in development mode.
     * @return A new FilterRegistration bean.
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
