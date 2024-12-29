package id.purwandi.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.web.server.ServerWebExchange;

import lombok.Data;
import reactor.core.publisher.Mono;

public class IPGatewayFilterFactory extends AbstractGatewayFilterFactory<IPGatewayFilterFactory.Config> {

    public IPGatewayFilterFactory() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            }));
        };
    }

    @Data
    public static class Config {
        private String fromIPBlock;
    }

}
