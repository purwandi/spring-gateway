package id.purwandi.gateway.filter;


import java.util.Arrays;
import java.util.HashMap;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import id.purwandi.gateway.model.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    public AuthorizationGatewayFilterFactory() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
            final String tokenScopes = exchange.getAttribute("scope");
            if (tokenScopes == null) {
                return Response.Error(exchange, HttpStatus.BAD_REQUEST, "Undefined scope context");
            }

            final String[] scopes = tokenScopes.split(" ");
            for (String tkscope : scopes) {
                if (Arrays.asList(config.getScopes().split(" ")).contains(tkscope)) {
                    return chain.filter(exchange);
                }
            }

            HashMap<String, String> object = new HashMap<>();
            object.put("allowed", config.getScopes());
            object.put("given", tokenScopes);

            return Response.Error(exchange, HttpStatus.FORBIDDEN, object);

        };
    }

    @Data
    public static class Config {
        private String scopes;
    }
}
