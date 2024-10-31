package com.example.labtest.repository;

import com.example.labtest.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {

    // Find LabResult by its status
    List<LabResult> findByStatus(String status);

    // Find LabResult by patient ID (useful for patient-specific queries)
    List<LabResult> findByPatientId(String patientId);

    // Find a LabResult by its ID (overrides default findById to return Optional<LabResult>)
    Optional<LabResult> findById(Long id);
}