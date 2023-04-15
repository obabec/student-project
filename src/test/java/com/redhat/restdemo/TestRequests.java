package com.redhat.restdemo;

import com.redhat.restdemo.model.entity.Author;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class TestRequests {
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private void assertStatus(HttpStatus httpStatus) {
        assert (httpStatus.value() >= 200);
        assert (httpStatus.value() < 300);
    }

    public ResponseEntity<String> post(String url, Object object) {
        HttpEntity<Object> request = new HttpEntity<>(object, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        assertStatus(response.getStatusCode());
        return response;
    }

    public ResponseEntity<String> get(String url) {
        HttpEntity<Object> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        assertStatus(response.getStatusCode());
        return response;
    }

    public ResponseEntity<String> delete(String url) {
        HttpEntity<Object> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        assertStatus(response.getStatusCode());
        return response;
    }
}
