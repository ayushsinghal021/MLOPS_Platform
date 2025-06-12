package com.mlops.training.controller;

import com.mlops.training.entity.Model;
import com.mlops.training.entity.TrainingJob;
import com.mlops.training.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @PostMapping("/training/jobs")
    public ResponseEntity<?> createTrainingJob(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Long datasetId = Long.valueOf(request.get("datasetId").toString());

            TrainingJob job = trainingService.createTrainingJob(name, datasetId);
            return ResponseEntity.ok(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error creating training job: " + e.getMessage());
        }
    }

    @GetMapping("/training/jobs/{id}")
    public ResponseEntity<?> getTrainingJob(@PathVariable Long id) {
        return trainingService.getTrainingJob(id)
                .map(job -> ResponseEntity.ok(job))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/models")
    public ResponseEntity<List<Model>> getAllModels() {
        return ResponseEntity.ok(trainingService.getAllModels());
    }
}