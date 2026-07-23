package com.tl.repository;

import com.tl.domain.entity.TransmissionLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransmissionLineRepository extends JpaRepository<TransmissionLine, Long> {

    Optional<TransmissionLine> findByLineCode(String lineCode);

    Page<TransmissionLine> findByVoltageLevel(Integer voltageLevel, Pageable pageable);

    @Query(value = "SELECT l FROM TransmissionLine l WHERE ST_Intersects(l.path, ST_GeomFromText(?1, 4326)) = true")
    Page<TransmissionLine> findIntersectingLines(String geometry, Pageable pageable);
}