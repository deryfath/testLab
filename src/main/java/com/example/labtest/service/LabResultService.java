package com.example.labtest.service;

import com.example.labtest.entity.LabResult;
import com.example.labtest.repository.LabResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class LabResultService {

    private final LabResultRepository labResultRepository;

    @Autowired
    public LabResultService(LabResultRepository labResultRepository) {
        this.labResultRepository = labResultRepository;
    }

    // Submit a new lab result for processing
    @Transactional
    public LabResult submitLabResult(String patientId, String testType, String sampleData) {
        LabResult labResult = new LabResult(patientId, testType, sampleData, "Pending", null);
        return labResultRepository.save(labResult);
    }

    // Get lab result status by ID
    public String getLabResultStatus(Long id) {
        Optional<LabResult> labResult = labResultRepository.findById(id);
        return labResult.map(LabResult::getStatus).orElse("Lab result not found");
    }

    // Retrieve the completed lab result by ID
    public Optional<LabResult> retrieveLabResult(Long id) {
        return labResultRepository.findById(id)
                .filter(labResult -> "Completed".equals(labResult.getStatus()));
    }

    // Upload and process each lab result entry from an Excel file
    @Async
    @Transactional
    public CompletableFuture<List<LabResult>> processExcelUpload(List<LabResult> labResults) {
        for (LabResult labResult : labResults) {
            processLabResult(labResult); // Initiate processing for each entry
        }
        return CompletableFuture.completedFuture(labResults);
    }

    // Process a single lab result
    @Async
    @Transactional
    public void processLabResult(LabResult labResult) {
        try {
            // Set initial status to Processing
            labResult.setStatus("Processing");
            labResultRepository.save(labResult);

            // Simulate a long-running task (e.g., lab analysis)
            Thread.sleep(3000); // Simulated delay for processing

            // Update result data and status after processing
            labResult.setResultData("Analysis complete for " + labResult.getTestType());
            labResult.setStatus("Completed");
            labResultRepository.save(labResult);

        } catch (InterruptedException e) {
            labResult.setStatus("Failed");
            labResultRepository.save(labResult);
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }

    // Retrieve all lab results by status
    public List<LabResult> findLabResultsByStatus(String status) {
        return labResultRepository.findByStatus(status);
    }
}
