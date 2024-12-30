package id.purwandi.gateway.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import id.purwandi.gateway.config.JWTConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class HttpClientConfiguration {

    @Autowired
    JWTConfig config;

    @Bean(name = "JWTHttpClient")
    HttpClient JWTHttpClient() {
        HttpClient client = HttpClient.create().baseUrl(config.getIntrospectUrl());

        log.info("Loaded: jwt http client...");

        return client;
    }
}
