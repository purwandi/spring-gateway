package id.purwandi.gateway.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

public class Response {

    public static Mono<Void> Error(ServerWebExchange exchange, HttpStatus status, Object message) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", status.value());
        errors.put("error", status.getReasonPhrase());
        errors.put("message", message);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = new ObjectMapper().writeValueAsBytes(errors);
            DataBuffer buff = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buff));
        } catch (Exception e) {
            return response.setComplete();
        }
    }

}
