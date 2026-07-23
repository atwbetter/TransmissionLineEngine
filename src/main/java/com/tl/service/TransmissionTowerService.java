package com.tl.service;

import com.tl.domain.dto.TransmissionTowerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransmissionTowerService {

    TransmissionTowerDTO create(TransmissionTowerDTO dto);

    TransmissionTowerDTO update(Long id, TransmissionTowerDTO dto);

    Optional<TransmissionTowerDTO> findById(Long id);

    Optional<TransmissionTowerDTO> findByCode(String code);

    Page<TransmissionTowerDTO> findPage(Pageable pageable);

    Page<TransmissionTowerDTO> findByLineId(Long lineId, Pageable pageable);

    Page<TransmissionTowerDTO> findNearby(Double longitude, Double latitude, Double radiusKm, Pageable pageable);

    void delete(Long id);
}