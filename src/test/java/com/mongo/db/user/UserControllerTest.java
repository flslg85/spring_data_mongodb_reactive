package com.mongo.db.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    private WebClient webClient;
    private WebTestClient webTestClient;

    @Before
    public void setUp() {
        String baseUrl = "http://localhost:" + port;

        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    @Test
    public void getDomainClassConverter() {
        User newUser = new User().setName("ivory").setAge(35);
        User savedUser = this.webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .build())
                .body(Mono.just(newUser), User.class)
                .retrieve()
                .bodyToMono(User.class)
                .block();

        User user = this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users" + "/" + savedUser.getId())
                        .build())
                .exchange()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();

        assertEquals("반환되는 user 는 ivory 이어야 한다.", "ivory", user.getName());
    }

    @Test
    public void getUsersWithMongoTemplate() {
        List<User> users = this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/template")
                        .queryParam("name", "bob")
                        .queryParam("page", 0)
                        .queryParam("size", 2)
                        .build())
                .exchange()
                .returnResult(User.class)
                .getResponseBody()
                .collectList()
                .block();

        assertEquals("반환되는 user 는 bob 이어야 한다.", "bob", users.get(0).getName());
    }
}