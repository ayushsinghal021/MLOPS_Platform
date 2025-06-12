package com.mlops.training.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String modelPath;

    private Long trainingJobId;
    private Double accuracy;
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Model() {
        this.createdAt = LocalDateTime.now();
    }

    public Model(String name, String version, String modelPath, Long trainingJobId) {
        this();
        this.name = name;
        this.version = version;
        this.modelPath = modelPath;
        this.trainingJobId = trainingJobId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getModelPath() { return modelPath; }
    public void setModelPath(String modelPath) { this.modelPath = modelPath; }

    public Long getTrainingJobId() { return trainingJobId; }
    public void setTrainingJobId(Long trainingJobId) { this.trainingJobId = trainingJobId; }

    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
