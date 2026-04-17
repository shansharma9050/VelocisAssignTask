package com.example.demo;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CookieRelayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        var request = exchange.getRequest();

        // Get cookies from browser request
        String cookieHeader = request.getHeaders().getFirst(HttpHeaders.COOKIE);

        if (cookieHeader != null) {

            var mutatedRequest = request.mutate()
                    .header(HttpHeaders.COOKIE, cookieHeader)
                    .build();

            return chain.filter(
                    exchange.mutate().request(mutatedRequest).build()
            );
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;  // run before routing
    }
}