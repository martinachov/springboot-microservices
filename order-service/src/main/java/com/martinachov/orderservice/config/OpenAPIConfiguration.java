package com.martinachov.orderservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(title = "Order Service API", version = "1.0.0"),
        servers = {@Server(url = "http://localhost:8081")},
        tags = {@Tag(name = "Order", description = "Order API")}
)
public class OpenAPIConfiguration {
}
