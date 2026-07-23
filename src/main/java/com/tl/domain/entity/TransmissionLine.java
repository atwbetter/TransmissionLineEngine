package com.tl.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDateTime;

@Entity
@Table(name = "transmission_line", indexes = {
    @Index(name = "idx_line_path", columnList = "path"),
    @Index(name = "idx_line_code", columnList = "line_code")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransmissionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String lineCode;

    @Column(nullable = false, length = 100)
    private String lineName;

    @Column(columnDefinition = "geography(LineString,4326)")
    private LineString path;

    @Column(length = 100)
    private String startPoint;

    @Column(length = 100)
    private String endPoint;

    @Column(name = "voltage_level")
    private Integer voltageLevel;

    private Double length;

    private Integer circuits;

    @Column(columnDefinition = "SMALLINT DEFAULT 0")
    private Integer status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}