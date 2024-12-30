package id.purwandi.gateway.filter;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.Data;

@Component
public class CacheGatewayFilterFactory extends AbstractGatewayFilterFactory<CacheGatewayFilterFactory.Config> {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public CacheGatewayFilterFactory() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
            redisTemplate.opsForValue().set("key", "value", Duration.ofMinutes(1));

            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {}
}
