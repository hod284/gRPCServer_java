package com.example.gRPCBackend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitiallzer {

    private JdbcTemplate jdbcTemplate;
    public DatabaseInitiallzer(JdbcTemplate jTemplate) {
        jdbcTemplate = jTemplate;
    }
    @PostConstruct
    private void createPlateRecordsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS carinformation (
                id SERIAL PRIMARY KEY,
                license_plate VARCHAR(20) NOT NULL,
                bad_point BIGINT,
                driver_owner VARCHAR(255)
            )
        """;
        
        jdbcTemplate.execute(sql);
        System.out.println("✅ plate_records 테이블 준비 완료");
    }


}