package id.purwandi.gateway.bootstrap;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import id.purwandi.gateway.config.JWTConfig;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JWTConfiguration {

    @Autowired
    private JWTConfig config;

    @Bean
    DefaultJWTProcessor<SecurityContext> getJWTProcessor() {
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(JOSEObjectType.JWT));

        log.atInfo()
            .addKeyValue("issuer", config.getIssuer())
            .addKeyValue("jwks_url", config.getJwksUrl())
            .addKeyValue("ttl", config.getTtl())
            .addKeyValue("refresh_timeout", config.getRefreshTimeout())
            .addKeyValue("outage_ttl", config.getOutageTtl())
            .addKeyValue("leeway", config.getLeeway())
            .log("issuer from bean ");

        final long ttl              = config.getTtl() * 60 * 1000;
        final long refreshTimeout   = config.getRefreshTimeout() * 60 * 1000;
        final long outageTtl        = config.getOutageTtl() * 60 * 1000;
        final int leeway            = config.getLeeway() * 60;

        try {
            JWKSource<SecurityContext> keySource = JWKSourceBuilder.create(new URL(config.getJwksUrl()))
                .retrying(true)
                .cache(ttl, refreshTimeout)
                .outageTolerant(outageTtl)
                .build();
            JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);

            DefaultJWTClaimsVerifier<SecurityContext> verifier = new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder().issuer(config.getIssuer()).build(),
                new HashSet<>(Arrays.asList(
                    JWTClaimNames.ISSUER,
                    JWTClaimNames.SUBJECT,
                    JWTClaimNames.ISSUED_AT,
                    JWTClaimNames.EXPIRATION_TIME
                ))
            );
            verifier.setMaxClockSkew(-leeway);

            jwtProcessor.setJWSKeySelector(keySelector);
            jwtProcessor.setJWTClaimsSetVerifier(verifier);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(1);
        }

        return jwtProcessor;
    }
}
