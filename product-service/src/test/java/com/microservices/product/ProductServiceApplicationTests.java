package com.microservices.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	protected Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		// Start MongoDB container before tests
		mongoDBContainer.start();
	}

	@Test
	void contextLoads() {
		String requestBody = """
                {
                    "name" : "iphone_16_pro",
                    "description" : "new",
                    "price" : 1000
                }
                """;
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when().post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("iphone_16_pro"))
				.body("description", Matchers.equalTo("new"))
				.body("price", Matchers.equalTo(1000));
	}
}
