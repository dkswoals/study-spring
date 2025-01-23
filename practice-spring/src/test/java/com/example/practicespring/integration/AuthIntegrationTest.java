package com.example.practicespring.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.practicespring.auth.dto.LoginRequest;
import com.example.practicespring.auth.dto.RefreshRequest;
import com.example.practicespring.auth.dto.RegisterRequest;
import com.example.practicespring.auth.dto.TokenResponse;
import com.example.practicespring.global.exception.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @LocalServerPort
    int port;

    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_PASSWORD = "password";

    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final RegisterRequest registerRequest = new RegisterRequest(TEST_EMAIL, TEST_PASSWORD);
    private final LoginRequest loginRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);


    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void testRefreshScenario() throws InterruptedException, JsonProcessingException {
        restTemplate.postForObject("http://localhost:" + port + "/api/auth/register",
            registerRequest, ResponseEntity.class);

        var tokenResponse = restTemplate.postForObject(
            "http://localhost:" + port + "/api/auth/login", loginRequest, TokenResponse.class);


        Thread.sleep(3000);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenResponse.accessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                "http://localhost:" + port + "/api/test/auth",
                HttpMethod.GET,
                entity,
                ErrorResponse.class
            );
        } catch (HttpClientErrorException e) {
            ErrorResponse errorResponse = objectMapper.readValue(
                e.getResponseBodyAsString(),
                ErrorResponse.class
            );
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            assertEquals("Invalid or expired token", errorResponse.errorMessage());
        }

        RefreshRequest refreshRequest = new RefreshRequest(tokenResponse.refreshToken());

        TokenResponse refreshed = restTemplate.postForObject(
            "http://localhost:" + port + "/api/auth/refresh", refreshRequest, TokenResponse.class);

        System.out.println(refreshed);

        HttpHeaders newHeader = new HttpHeaders();
        newHeader.add("Authorization", "Bearer " + refreshed.accessToken());
        HttpEntity<?> newEntity = new HttpEntity<>(newHeader);

        ResponseEntity<String> expected = restTemplate.exchange(
            "http://localhost:" + port + "/api/test/auth",
            HttpMethod.GET,
            newEntity,
            String.class
        );

        System.out.println(expected.getBody());
    }

}
