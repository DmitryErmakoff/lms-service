package ru.d3m4k.lms.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import ru.d3m4k.lms.service.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
@SpringBootTest
class AuthControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:1234/lms_db");
//        registry.add("spring.datasource.username", () -> "bob");
//        registry.add("spring.datasource.password", () -> "apazev09");
//    }

    @Test
    @SneakyThrows
    void shouldAuthorizeUserSuccessfully() {
        String jsonBody = "    {\n" +
                "        \"login\": \"user\",\n" +
                "        \"password\": \"100\"\n" +
                "    }";

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("http://localhost:8080/auth")
                .andReturn();

        TokenResponse tokenResponse = objectMapper.readValue(response.getBody().asString(), TokenResponse.class);

        Response authToUser = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenResponse.getToken())
                .when()
                .get("http://localhost:8080/secured")
                .andReturn();

        authToUser.prettyPrint();

        assertThat(authToUser.getStatusCode()).isEqualTo(200);
    }

    @Test
    @SneakyThrows
    void shouldAuthorizeAdminSuccessfully() {
        String jsonBody = "    {\n" +
                "        \"login\": \"admin\",\n" +
                "        \"password\": \"100\"\n" +
                "    }";

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("http://localhost:8080/auth")
                .andReturn();

        TokenResponse tokenResponse = objectMapper.readValue(response.getBody().asString(), TokenResponse.class);

        Response authToAdmin = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenResponse.getToken())
                .body(response.getBody().asString())
                .when()
                .get("http://localhost:8080/admin");

        authToAdmin.prettyPrint();
        assertThat(authToAdmin.getStatusCode()).isEqualTo(200);
    }

}