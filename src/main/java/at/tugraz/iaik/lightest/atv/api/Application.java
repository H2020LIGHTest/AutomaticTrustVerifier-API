package at.tugraz.iaik.lightest.atv.api;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.security.Security;

@SpringBootApplication
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        Security.setProperty("keystore.type", "jks");
        return application.sources(Application.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
