package com.tl.service;

import com.tl.domain.dto.TransmissionLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransmissionLineService {

    TransmissionLineDTO create(TransmissionLineDTO dto);

    TransmissionLineDTO update(Long id, TransmissionLineDTO dto);

    Optional<TransmissionLineDTO> findById(Long id);

    Optional<TransmissionLineDTO> findByCode(String code);

    Page<TransmissionLineDTO> findPage(Pageable pageable);

    void delete(Long id);
}