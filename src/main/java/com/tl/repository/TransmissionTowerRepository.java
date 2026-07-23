package com.tl.repository;

import com.tl.domain.entity.TransmissionTower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransmissionTowerRepository extends JpaRepository<TransmissionTower, Long> {

    Optional<TransmissionTower> findByTowerCode(String towerCode);

    Page<TransmissionTower> findByLineId(Long lineId, Pageable pageable);

    Page<TransmissionTower> findByVoltageLevel(Integer voltageLevel, Pageable pageable);

    @Query(value = "SELECT t FROM TransmissionTower t WHERE ST_DWithin(t.location, ST_GeomFromText(?1, 4326), ?2) = true")
    Page<TransmissionTower> findTowersNear(String point, Double distance, Pageable pageable);
}