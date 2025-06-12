package com.mlops.deployment.service;

import com.mlops.deployment.entity.Deployment;
import com.mlops.deployment.repository.DeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeploymentService {

    @Autowired
    private DeploymentRepository deploymentRepository;

    // In-memory store for active model processes (in production, use Redis or similar)
    private final Map<Long, Process> activeDeployments = new ConcurrentHashMap<>();

    public Deployment createDeployment(String name, Long modelId, String modelPath) {
        Deployment deployment = new Deployment(name, modelId, modelPath);
        deployment.setVersion("v1.0");

        Deployment savedDeployment = deploymentRepository.save(deployment);

        // Start deployment asynchronously
        CompletableFuture.runAsync(() -> deployModel(savedDeployment));

        return savedDeployment;
    }

    public Optional<Deployment> getDeployment(Long id) {
        return deploymentRepository.findById(id);
    }

    public List<Deployment> getAllDeployments() {
        return deploymentRepository.findAll();
    }

    public Map<String, Object> predict(Long deploymentId, Map<String, Object> inputData) {
        Optional<Deployment> deploymentOpt = deploymentRepository.findById(deploymentId);

        if (deploymentOpt.isEmpty()) {
            throw new RuntimeException("Deployment not found");
        }

        Deployment deployment = deploymentOpt.get();

        if (deployment.getStatus() != Deployment.DeploymentStatus.ACTIVE) {
            throw new RuntimeException("Deployment is not active");
        }

        // Simulate model prediction
        // In a real implementation, this would call your model serving endpoint
        return simulateModelPrediction(inputData);
    }

    private void deployModel(Deployment deployment) {
        try {
            deployment.setStatus(Deployment.DeploymentStatus.DEPLOYING);
            deploymentRepository.save(deployment);

            // Simulate model deployment
            // In practice, this would start a model serving process (Flask, FastAPI, etc.)
            ProcessBuilder pb = new ProcessBuilder("python", "-c",
                    "import time; print('Starting model server...'); time.sleep(3); print('Model server started successfully')");

            Process process = pb.start();

            // Store the process reference
            activeDeployments.put(deployment.getId(), process);

            StringBuilder logs = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logs.append(line).append("\n");
                }
            }

            // For MVP, we'll just mark as active after the process completes
            // In production, you'd have a health check endpoint
            deployment.setStatus(Deployment.DeploymentStatus.ACTIVE);
            deployment.setEndpoint("/predict/" + deployment.getId());
            deployment.setDeployedAt(LocalDateTime.now());
            deploymentRepository.save(deployment);

        } catch (IOException e) {
            deployment.setStatus(Deployment.DeploymentStatus.FAILED);
            deploymentRepository.save(deployment);
        }
    }

    private Map<String, Object> simulateModelPrediction(Map<String, Object> inputData) {
        // Simulate prediction logic
        // In practice, this would call your actual model

        double prediction = Math.random(); // Random prediction for demo
        String predictedClass = prediction > 0.5 ? "positive" : "negative";

        return Map.of(
                "prediction", predictedClass,
                "confidence", prediction,
                "timestamp", LocalDateTime.now().toString(),
                "inputFeatures", inputData.size()
        );
    }
}