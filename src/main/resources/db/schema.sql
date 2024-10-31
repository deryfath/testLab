CREATE TABLE IF NOT EXISTS lab_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(50) NOT NULL,
    test_type VARCHAR(100) NOT NULL,
    sample_data TEXT,
    status VARCHAR(20) DEFAULT 'Pending',
    result_data TEXT
);