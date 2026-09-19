package com.api_gateway.api_gateway.configs;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtHeaderFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .cast(JwtAuthenticationToken.class)
                .flatMap(auth -> {
                    String username = auth.getToken().getSubject();
                    String role = auth.getToken().getClaim("role");

                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header("X-User", username)
                            .header("X-Role", role)
                            .build();

                    return chain.filter(
                            exchange.mutate().request(request).build()
                    );

                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
