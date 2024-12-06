package by.it_academy.audit_service.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.jwt")
public class JWTProperty {

    private String secret;
    private String issuer;

}
