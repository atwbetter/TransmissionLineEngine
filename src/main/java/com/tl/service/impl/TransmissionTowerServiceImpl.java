package com.tl.service.impl;

import com.tl.domain.dto.TransmissionTowerDTO;
import com.tl.domain.entity.TransmissionTower;
import com.tl.repository.TransmissionTowerRepository;
import com.tl.service.TransmissionTowerService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransmissionTowerServiceImpl implements TransmissionTowerService {

    private final TransmissionTowerRepository repository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    @Transactional
    public TransmissionTowerDTO create(TransmissionTowerDTO dto) {
        TransmissionTower tower = convertToEntity(dto);
        TransmissionTower saved = repository.save(tower);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public TransmissionTowerDTO update(Long id, TransmissionTowerDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    if (dto.getTowerName() != null) {
                        existing.setTowerName(dto.getTowerName());
                    }
                    if (dto.getLongitude() != null && dto.getLatitude() != null) {
                        Point point = geometryFactory.createPoint(
                                new Coordinate(dto.getLongitude(), dto.getLatitude()));
                        point.setSRID(4326);
                        existing.setLocation(point);
                    }
                    if (dto.getVoltageLevel() != null) {
                        existing.setVoltageLevel(dto.getVoltageLevel());
                    }
                    if (dto.getStatus() != null) {
                        existing.setStatus(dto.getStatus());
                    }
                    return convertToDTO(repository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("Tower not found: " + id));
    }

    @Override
    public Optional<TransmissionTowerDTO> findById(Long id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<TransmissionTowerDTO> findByCode(String code) {
        return repository.findByTowerCode(code).map(this::convertToDTO);
    }

    @Override
    public Page<TransmissionTowerDTO> findPage(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public Page<TransmissionTowerDTO> findByLineId(Long lineId, Pageable pageable) {
        return repository.findByLineId(lineId, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<TransmissionTowerDTO> findNearby(Double longitude, Double latitude, Double radiusKm, Pageable pageable) {
        String point = String.format("POINT(%f %f)", longitude, latitude);
        double distanceMeters = radiusKm * 1000;
        return repository.findTowersNear(point, distanceMeters, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private TransmissionTower convertToEntity(TransmissionTowerDTO dto) {
        TransmissionTower tower = new TransmissionTower();
        tower.setTowerCode(dto.getTowerCode());
        tower.setTowerName(dto.getTowerName());
        tower.setVoltageLevel(dto.getVoltageLevel());
        tower.setTowerType(dto.getTowerType());
        tower.setHeight(dto.getHeight());
        tower.setFoundationType(dto.getFoundationType());
        tower.setLineId(dto.getLineId());
        tower.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        tower.setDescription(dto.getDescription());

        if (dto.getLongitude() != null && dto.getLatitude() != null) {
            Point point = geometryFactory.createPoint(
                    new Coordinate(dto.getLongitude(), dto.getLatitude()));
            point.setSRID(4326);
            tower.setLocation(point);
        }

        return tower;
    }

    private TransmissionTowerDTO convertToDTO(TransmissionTower tower) {
        return TransmissionTowerDTO.builder()
                .id(tower.getId())
                .towerCode(tower.getTowerCode())
                .towerName(tower.getTowerName())
                .longitude(tower.getLocation() != null ? tower.getLocation().getX() : null)
                .latitude(tower.getLocation() != null ? tower.getLocation().getY() : null)
                .voltageLevel(tower.getVoltageLevel())
                .towerType(tower.getTowerType())
                .height(tower.getHeight())
                .foundationType(tower.getFoundationType())
                .lineId(tower.getLineId())
                .status(tower.getStatus())
                .description(tower.getDescription())
                .createdAt(tower.getCreatedAt())
                .updatedAt(tower.getUpdatedAt())
                .build();
    }
}