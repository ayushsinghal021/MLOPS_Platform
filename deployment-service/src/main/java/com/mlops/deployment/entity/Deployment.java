package com.mlops.deployment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deployments")
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long modelId;

    @Column(nullable = false)
    private String modelPath;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    private String endpoint;
    private String version;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deployedAt;

    public enum DeploymentStatus {
        PENDING, DEPLOYING, ACTIVE, FAILED, STOPPED
    }

    // Constructors
    public Deployment() {
        this.createdAt = LocalDateTime.now();
        this.status = DeploymentStatus.PENDING;
    }

    public Deployment(String name, Long modelId, String modelPath) {
        this();
        this.name = name;
        this.modelId = modelId;
        this.modelPath = modelPath;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }

    public String getModelPath() { return modelPath; }
    public void setModelPath(String modelPath) { this.modelPath = modelPath; }

    public DeploymentStatus getStatus() { return status; }
    public void setStatus(DeploymentStatus status) { this.status = status; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDeployedAt() { return deployedAt; }
    public void setDeployedAt(LocalDateTime deployedAt) { this.deployedAt = deployedAt; }
}