package ifpr.edu.br.mooc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    
    private String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private Long expirationHours = 12L; // 12 hours
    private String issuer = "MOOC IFPR";

}