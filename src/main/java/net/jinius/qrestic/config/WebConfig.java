package net.jinius.qrestic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring MVC Configuration.
 * Implements {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}, which provides convenient callbacks that allow us to customize aspects
 * of the Spring Web MVC framework. These callbacks allow us to register custom interceptors, message converters,
 * argument resovlers, a validator, resource handling, and other things.
 *
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "net.jinius.qrestic.web")
public class WebConfig extends WebMvcConfigurerAdapter {

}
