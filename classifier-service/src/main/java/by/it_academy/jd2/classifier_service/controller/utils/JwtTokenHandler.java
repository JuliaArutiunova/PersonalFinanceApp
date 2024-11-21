package by.it_academy.jd2.classifier_service.controller.utils;


import by.it_academy.jd2.classifier_service.config.property.JWTProperty;
import by.it_academy.lib.dto.TokenInfoDTO;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenHandler {

    private final JWTProperty jwtProperty;


    public JwtTokenHandler(JWTProperty jwtProperty) {
        this.jwtProperty = jwtProperty;

    }



    public TokenInfoDTO getTokenInfo(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperty.getSecret())
                .parseClaimsJws(token)
                .getBody();

        return new TokenInfoDTO(claims.getSubject(), (String) claims.get("role"));
    }

    public boolean validate(String token) {

        try {
            Jwts.parser().setSigningKey(jwtProperty.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException | IllegalArgumentException | MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException ex) {
            return false;
        }

    }


}
