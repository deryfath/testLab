package com.example.labtest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lab_results")
public class LabResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientId;
    private String testType;
    private String sampleData;
    private String status;
    private String resultData;

    // Default constructor
    public LabResult() {
    }

    // Constructor with fields
    public LabResult(String patientId, String testType, String sampleData, String status, String resultData) {
        this.patientId = patientId;
        this.testType = testType;
        this.sampleData = sampleData;
        this.status = status;
        this.resultData = resultData;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getSampleData() {
        return sampleData;
    }

    public void setSampleData(String sampleData) {
        this.sampleData = sampleData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "LabResult{" +
                "id=" + id +
                ", patientId='" + patientId + '\'' +
                ", testType='" + testType + '\'' +
                ", sampleData='" + sampleData + '\'' +
                ", status='" + status + '\'' +
                ", resultData='" + resultData + '\'' +
                '}';
    }
}
