/**
 * 
 */
package com.zenika;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mangofactory.swagger.configuration.DocumentationConfig;
import com.zenika.business.InMemoryContactService;
import com.zenika.web.ContactController;

/**
 * @author acogoluegnes
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses={InMemoryContactService.class,ContactController.class})
@Import(DocumentationConfig.class)
public class ZenContactConfiguration extends WebMvcConfigurerAdapter {

	@Bean static  public PropertySourcesPlaceholderConfigurer myPropertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        Resource[] resourceLocations = new Resource[] {
                new ClassPathResource("swagger.properties")
        };
        p.setLocations(resourceLocations);
        return p;
    }

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/swagger-ui/**")
            	.addResourceLocations("classpath:/swagger-ui/");
    }
	
}
