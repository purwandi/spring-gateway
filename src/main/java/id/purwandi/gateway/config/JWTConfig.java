package id.purwandi.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@RequiredArgsConstructor
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
