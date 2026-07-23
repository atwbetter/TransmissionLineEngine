package com.tl.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "transmission_tower", indexes = {
    @Index(name = "idx_tower_location", columnList = "location"),
    @Index(name = "idx_tower_voltage", columnList = "voltage_level")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransmissionTower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String towerCode;

    @Column(length = 100)
    private String towerName;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;

    @Column(name = "voltage_level")
    private Integer voltageLevel;

    @Column(length = 50)
    private String towerType;

    private Double height;

    @Column(length = 50)
    private String foundationType;

    @Column(name = "line_id")
    private Long lineId;

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