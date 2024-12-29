package id.purwandi.gateway.filter;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import id.purwandi.gateway.model.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    @Autowired
    DefaultJWTProcessor<SecurityContext> processor;

    public AuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.debug("filters: authentication");

            ServerHttpRequest request = exchange.getRequest();

            final String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
                return Response.Error(exchange, HttpStatus.UNAUTHORIZED, "Missing authentication headers");
            }

            final String token = header.split(" ")[1].trim();
            SecurityContext ctx = null;
            JWTClaimsSet claims;

            try {
                claims = processor.process(token, ctx);
                log.debug(claims.toJSONObject().toString());

                exchange.getAttributes().put("scope", claims.getClaimAsString("scope"));

            } catch (ParseException | BadJOSEException e) {
                log.error(token, e);
                return Response.Error(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
            } catch (JOSEException e) {
                log.error(token, e);
                return Response.Error(exchange, HttpStatus.UNAUTHORIZED, "Token processing error");
            } catch (Exception e) {
                log.error(token, e);
                return Response.Error(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Unkown server error");
            }

            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {}
}
