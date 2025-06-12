package com.mlops.data.controller;

import com.mlops.data.entity.Dataset;
import com.mlops.data.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/datasets")
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping
    public ResponseEntity<?> uploadDataset(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            Dataset dataset = dataService.saveDataset(file);
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDataset(@PathVariable Long id) {
        return dataService.getDataset(id)
                .map(dataset -> ResponseEntity.ok(dataset))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Dataset>> getAllDatasets() {
        return ResponseEntity.ok(dataService.getAllDatasets());
    }
}