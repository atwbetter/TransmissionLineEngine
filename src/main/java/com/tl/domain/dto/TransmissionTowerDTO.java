package com.tl.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransmissionTowerDTO {

    private Long id;
    private String towerCode;
    private String towerName;
    private Double longitude;
    private Double latitude;
    private Integer voltageLevel;
    private String towerType;
    private Double height;
    private String foundationType;
    private Long lineId;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}