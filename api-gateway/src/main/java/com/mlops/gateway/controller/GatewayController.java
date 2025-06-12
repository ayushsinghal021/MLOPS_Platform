package com.mlops.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class GatewayController {

    @Value("${services.data.url:http://localhost:8081}")
    private String dataServiceUrl;

    @Value("${services.training.url:http://localhost:8082}")
    private String trainingServiceUrl;

    @Value("${services.deployment.url:http://localhost:8083}")
    private String deploymentServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Data Service Routes
    @RequestMapping(value = "/datasets/**", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> forwardToDataService(
            HttpServletRequest request, @RequestBody(required = false) String body) {
        String path = request.getRequestURI().replace("/api", "");
        return forwardRequest(dataServiceUrl + path, request.getMethod(), body);
    }

    // Training Service Routes
    @RequestMapping(value = {"/training/**", "/models/**"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> forwardToTrainingService(
            HttpServletRequest request, @RequestBody(required = false) String body) {
        String path = request.getRequestURI().replace("/api", "");
        return forwardRequest(trainingServiceUrl + path, request.getMethod(), body);
    }

    // Deployment Service Routes
    @RequestMapping(value = {"/deployments/**", "/predict/**"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> forwardToDeploymentService(
            HttpServletRequest request, @RequestBody(required = false) String body) {
        String path = request.getRequestURI().replace("/api", "");
        return forwardRequest(deploymentServiceUrl + path, request.getMethod(), body);
    }

    private ResponseEntity<String> forwardRequest(String url, String method, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            if ("POST".equals(method)) {
                return restTemplate.postForEntity(url, entity, String.class);
            } else {
                return restTemplate.getForEntity(url, String.class);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}