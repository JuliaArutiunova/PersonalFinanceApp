package by.it_academy.audit_service.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }
}