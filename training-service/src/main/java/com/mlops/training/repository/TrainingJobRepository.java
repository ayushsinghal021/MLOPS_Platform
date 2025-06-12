package com.mlops.training.repository;

import com.mlops.training.entity.TrainingJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingJobRepository extends JpaRepository<TrainingJob, Long> {
}