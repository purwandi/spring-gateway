package id.purwandi.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@AllArgsConstructor
public class JWTConfig {
    private String jwksUrl;
    private String issuer;
    private String introspectUrl;
    private Integer ttl;
    private Integer refreshTimeout;
    private Integer outageTtl;
    private Integer leeway;
    private String cliendID;
    private String cliendSecret;
}
