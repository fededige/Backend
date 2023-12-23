package com.progettoTAASS.apiGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
	@Value("${catalog_microservice_url}")
	private String catalog_microservice_url;

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route( p -> p
						.path("/catalog/**")
						.filters(f -> f.rewritePath("/catalog/(?<segment>.*)", "/${segment}"))
						.uri(catalog_microservice_url))
				.build();
	}
}