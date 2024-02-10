package com.progettoTAASS.apiGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class ApiGatewayApplication {

	@Value("${user_microservice_url}")
	private String user_microservice_url;
	@Value("${catalog_microservice_url}")
	private String catalog_microservice_url;
	@Value("${reservation_microservice_url}")
	private String reservation_microservice_url;
	@Value("${review_microservice_url}")
	private String review_microservice_url;

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
				.route( p -> p
						.path("/user/**")
						.filters(f -> f.rewritePath("/user/(?<segment>.*)", "/${segment}"))
						.uri(user_microservice_url))
				.route( p -> p
						.path("/reservation/**")
						.filters(f -> f.rewritePath("/reservation/(?<segment>.*)", "/${segment}"))
						.uri(reservation_microservice_url))
				.route( p -> p
						.path("/review/**")
						.filters(f -> f.rewritePath("/review/(?<segment>.*)", "/${segment}"))
						.uri(review_microservice_url))
				.build();
	}

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Collections.singletonList("*"));
		corsConfig.setMaxAge(8000L);
		corsConfig.addAllowedMethod("*");
		corsConfig.addAllowedHeader("*");

		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}
}