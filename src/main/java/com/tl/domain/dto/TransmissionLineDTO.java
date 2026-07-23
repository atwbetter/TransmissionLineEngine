package com.tl.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransmissionLineDTO {

    private Long id;
    private String lineCode;
    private String lineName;
    private List<CoordinateDTO> coordinates;
    private String startPoint;
    private String endPoint;
    private Integer voltageLevel;
    private Double length;
    private Integer circuits;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoordinateDTO {
        private Double longitude;
        private Double latitude;
    }
}