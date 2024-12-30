package id.purwandi.gateway.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.database}")
    private Integer database;

    @Bean
    RedisConnectionFactory getConnectionFactory(RedisStandaloneConfiguration clientConf) {
        final ClientResources clientResource = ClientResources.builder().build();
        final ClientOptions clientOptions = ClientOptions.builder()
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .pingBeforeActivateConnection(true)
            .autoReconnect(true)
            .build();

        LettuceClientConfiguration conf = LettuceClientConfiguration.builder()
            .clientResources(clientResource)
            .clientOptions(clientOptions)
            .build();

        return new LettuceConnectionFactory(clientConf, conf);
    }

    @Bean
    RedisTemplate<String, Object> getRedisTemplate() {
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration(host, port);
        conf.setPassword(password);
        conf.setDatabase(database);

        log.info("Loaded: redis template...");

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getConnectionFactory(conf));
        return template;
    }

}
