package com.progettoTAASS.apiGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
	/*
	@Value("${book_microservice_url}")
	private String book_microservice_url;
	@Value("${author_microservice_url}")
	private String author_microservice_url;
	*/

	@Value("${user_microservice_url}")
	private String user_microservice_url;

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		/*
		return builder.routes()
				.route( p -> p
						.path("/book/**")
						.filters(f -> f.rewritePath("/book/(?<segment>.*)", "/${segment}"))
						.uri(book_microservice_url))
				.route( p -> p
						.path("/author/**")
						.filters(f -> f.rewritePath("/author/(?<segment>.*)", "/${segment}"))
						.uri(author_microservice_url))
				.build();
		 */
		return builder.routes()
				.route( p -> p
						.path("/user/**")
						.filters(f -> f.rewritePath("/user/(?<segment>.*)", "/${segment}"))
						.uri(user_microservice_url))
				.build();
	}
}