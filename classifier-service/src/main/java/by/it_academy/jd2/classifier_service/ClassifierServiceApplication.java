package by.it_academy.jd2.classifier_service;

import by.it_academy.jd2.classifier_service.config.property.JWTProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties({JWTProperty.class})
@EnableTransactionManagement
public class ClassifierServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassifierServiceApplication.class, args);
	}

}
