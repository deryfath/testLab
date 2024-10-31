package com.example.labtest.controller;

import com.example.labtest.entity.ApiResponse;
import com.example.labtest.entity.LabResult;
import com.example.labtest.repository.LabResultRepository;
import com.example.labtest.service.LabResultService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lab-results")
public class LabResultController {

    @Autowired
    private LabResultService labResultService;

    @Autowired
    private LabResultRepository labResultRepository;

    @PostMapping
    public ResponseEntity<?> submitLabResult(@RequestBody LabResult labResult) {
        labResult.setStatus("Pending");
        labResultRepository.save(labResult);
        labResultService.processLabResult(labResult);
        return new ResponseEntity<>("Task submitted with ID: " + labResult.getId(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getLabResultStatus(@PathVariable Long id) {
        return labResultRepository.findById(id)
                .map(result -> ResponseEntity.ok(result.getStatus()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lab result not found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LabResult>> retrieveLabResult(@PathVariable Long id) {
        return labResultRepository.findById(id)
                .map(result -> ResponseEntity.ok(new ApiResponse<>("Lab result retrieved successfully", result)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Lab result not found", null)));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        List<String> taskIds = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                LabResult labResult = new LabResult();
                labResult.setPatientId(row.getCell(0).getStringCellValue());
                labResult.setTestType(row.getCell(1).getStringCellValue());
                labResult.setSampleData(row.getCell(2).getStringCellValue());
                labResult.setStatus("Pending");

                labResultRepository.save(labResult);
                labResultService.processLabResult(labResult);
                taskIds.add(String.valueOf(labResult.getId()));
            }
            workbook.close();
            return new ResponseEntity<>(taskIds, HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to parse Excel file", HttpStatus.BAD_REQUEST);
        }
    }
}
