package com.mlops.data.service;

import com.mlops.data.entity.Dataset;
import com.mlops.data.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    @Autowired
    private DatasetRepository datasetRepository;

    private final String uploadDir = "./data/uploads/";

    public DataService() {
        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public Dataset saveDataset(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), filePath);

        Dataset dataset = new Dataset(
                file.getOriginalFilename(),
                filePath.toString(),
                getFileExtension(file.getOriginalFilename()),
                file.getSize()
        );

        return datasetRepository.save(dataset);
    }

    public Optional<Dataset> getDataset(Long id) {
        return datasetRepository.findById(id);
    }

    public List<Dataset> getAllDatasets() {
        return datasetRepository.findAll();
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}