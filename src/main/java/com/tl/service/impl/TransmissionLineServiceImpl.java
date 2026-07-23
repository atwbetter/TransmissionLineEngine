package com.tl.service.impl;

import com.tl.domain.dto.TransmissionLineDTO;
import com.tl.domain.entity.TransmissionLine;
import com.tl.repository.TransmissionLineRepository;
import com.tl.service.TransmissionLineService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransmissionLineServiceImpl implements TransmissionLineService {

    private final TransmissionLineRepository repository;

    @Override
    @Transactional
    public TransmissionLineDTO create(TransmissionLineDTO dto) {
        TransmissionLine line = convertToEntity(dto);
        TransmissionLine saved = repository.save(line);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public TransmissionLineDTO update(Long id, TransmissionLineDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    if (dto.getLineName() != null) {
                        existing.setLineName(dto.getLineName());
                    }
                    if (dto.getVoltageLevel() != null) {
                        existing.setVoltageLevel(dto.getVoltageLevel());
                    }
                    if (dto.getStatus() != null) {
                        existing.setStatus(dto.getStatus());
                    }
                    return convertToDTO(repository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("Line not found: " + id));
    }

    @Override
    public Optional<TransmissionLineDTO> findById(Long id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<TransmissionLineDTO> findByCode(String code) {
        return repository.findByLineCode(code).map(this::convertToDTO);
    }

    @Override
    public Page<TransmissionLineDTO> findPage(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private TransmissionLine convertToEntity(TransmissionLineDTO dto) {
        TransmissionLine line = new TransmissionLine();
        line.setLineCode(dto.getLineCode());
        line.setLineName(dto.getLineName());
        line.setStartPoint(dto.getStartPoint());
        line.setEndPoint(dto.getEndPoint());
        line.setVoltageLevel(dto.getVoltageLevel());
        line.setLength(dto.getLength());
        line.setCircuits(dto.getCircuits());
        line.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        line.setDescription(dto.getDescription());
        return line;
    }

    private TransmissionLineDTO convertToDTO(TransmissionLine line) {
        return TransmissionLineDTO.builder()
                .id(line.getId())
                .lineCode(line.getLineCode())
                .lineName(line.getLineName())
                .startPoint(line.getStartPoint())
                .endPoint(line.getEndPoint())
                .voltageLevel(line.getVoltageLevel())
                .length(line.getLength())
                .circuits(line.getCircuits())
                .status(line.getStatus())
                .description(line.getDescription())
                .createdAt(line.getCreatedAt())
                .updatedAt(line.getUpdatedAt())
                .build();
    }
}