package g8.library.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@SpringBootApplication
public class ApplicationRunner {

	@RequestMapping(value = "/api/pingcheck")
	public String pingcheck() {
		return "API Service is up....";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() 
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")/* .allowedOrigins("http://localhost:8080") */;
            }
            
			/*
			 * @Override public void addCorsMappings(CorsRegistry registry) {
			 * registry.addMapping("/api/**") .allowedOrigins("http://domain2.com")
			 * .allowedMethods("PUT", "DELETE") .allowedHeaders("header1", "header2",
			 * "header3") .exposedHeaders("header1", "header2")
			 * .allowCredentials(false).maxAge(3600); }
			 */
        };
    }
	
	
	
}
