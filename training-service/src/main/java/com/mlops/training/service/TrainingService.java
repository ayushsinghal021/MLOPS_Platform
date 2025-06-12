package com.mlops.training.service;

import com.mlops.training.entity.Model;
import com.mlops.training.entity.TrainingJob;
import com.mlops.training.repository.ModelRepository;
import com.mlops.training.repository.TrainingJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TrainingService {

    @Autowired
    private TrainingJobRepository trainingJobRepository;

    @Autowired
    private ModelRepository modelRepository;

    public TrainingJob createTrainingJob(String name, Long datasetId) {
        TrainingJob job = new TrainingJob(name, datasetId);
        TrainingJob savedJob = trainingJobRepository.save(job);

        // Start training asynchronously
        CompletableFuture.runAsync(() -> runTraining(savedJob));

        return savedJob;
    }

    public Optional<TrainingJob> getTrainingJob(Long id) {
        return trainingJobRepository.findById(id);
    }

    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }

    private void runTraining(TrainingJob job) {
        try {
            job.setStatus(TrainingJob.JobStatus.RUNNING);
            trainingJobRepository.save(job);

            // Simulate training with a simple Python script call
            String modelPath = "./models/model_" + job.getId() + ".pkl";

            // This would call your actual Python training script
            ProcessBuilder pb = new ProcessBuilder("python", "-c",
                    "import time; import random; time.sleep(5); print('Training completed'); print(f'Accuracy: {random.uniform(0.8, 0.95):.3f}')");

            Process process = pb.start();

            StringBuilder logs = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logs.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                job.setStatus(TrainingJob.JobStatus.COMPLETED);
                job.setModelPath(modelPath);
                job.setLogs(logs.toString());
                job.setAccuracy(0.85 + Math.random() * 0.1); // Simulate accuracy
                job.setCompletedAt(LocalDateTime.now());

                // Create model record
                Model model = new Model(
                        job.getName() + "_model",
                        "v1.0",
                        modelPath,
                        job.getId()
                );
                model.setAccuracy(job.getAccuracy());
                modelRepository.save(model);

            } else {
                job.setStatus(TrainingJob.JobStatus.FAILED);
                job.setLogs("Training failed with exit code: " + exitCode);
            }

            trainingJobRepository.save(job);

        } catch (IOException | InterruptedException e) {
            job.setStatus(TrainingJob.JobStatus.FAILED);
            job.setLogs("Error during training: " + e.getMessage());
            trainingJobRepository.save(job);
        }
    }
}