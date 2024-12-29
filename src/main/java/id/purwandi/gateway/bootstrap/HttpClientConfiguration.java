package id.purwandi.gateway.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.purwandi.gateway.config.JWTConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class HttpClientConfiguration {

    @Autowired
    JWTConfig config;

    public HttpClient JWTHttpClient() {
        HttpClient client = HttpClient.create().baseUrl(config.getIntrospectUrl());

        log.info("config : jwt http client");

        return client;
    }
}
