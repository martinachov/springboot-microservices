package com.martinachov.productservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(title = "Product Service API", version = "1.0.0"),
        servers = {@Server(url = "http://localhost:8080")},
        tags = {@Tag(name = "Products", description = "Products CRUD")}
)
public class OpenAPIConfiguration {
}
