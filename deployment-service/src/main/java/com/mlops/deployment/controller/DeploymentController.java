package com.mlops.deployment.controller;

import com.mlops.deployment.entity.Deployment;
import com.mlops.deployment.service.DeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @PostMapping("/deployments")
    public ResponseEntity<?> createDeployment(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Long modelId = Long.valueOf(request.get("modelId").toString());
            String modelPath = (String) request.get("modelPath");

            Deployment deployment = deploymentService.createDeployment(name, modelId, modelPath);
            return ResponseEntity.ok(deployment);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error creating deployment: " + e.getMessage());
        }
    }

    @GetMapping("/deployments/{id}")
    public ResponseEntity<?> getDeployment(@PathVariable Long id) {
        return deploymentService.getDeployment(id)
                .map(deployment -> ResponseEntity.ok(deployment))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/deployments")
    public ResponseEntity<List<Deployment>> getAllDeployments() {
        return ResponseEntity.ok(deploymentService.getAllDeployments());
    }

    @PostMapping("/predict/{deploymentId}")
    public ResponseEntity<?> predict(@PathVariable Long deploymentId,
                                     @RequestBody Map<String, Object> inputData) {
        try {
            Map<String, Object> prediction = deploymentService.predict(deploymentId, inputData);
            return ResponseEntity.ok(prediction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Prediction error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Internal error: " + e.getMessage());
        }
    }
}