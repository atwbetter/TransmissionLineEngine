package com.transmission.service;

import com.transmission.dto.TransmissionTowerDTO;
import com.transmission.model.TransmissionTower;
import com.transmission.repository.TransmissionTowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TransmissionTowerService
 */
@DataJpaTest
@Import(TransmissionTowerService.class)
class TransmissionTowerServiceTest {

    @Autowired
    private TransmissionTowerService towerService;

    @Autowired
    private TransmissionTowerRepository towerRepository;

    private TransmissionTowerDTO testTower;

    @BeforeEach
    void setUp() {
        testTower = new TransmissionTowerDTO();
        testTower.setTowerCode("T001");
        testTower.setTowerName("Test Tower");
        testTower.setTowerType("Steel");
        testTower.setTowerHeight(45.5);
        testTower.setVoltageLevel("220kV");
        testTower.setLatitude(39.9042);
        testTower.setLongitude(116.4074);
        testTower.setStatus("ACTIVE");
    }

    @Test
    void testCreateTower() {
        TransmissionTowerDTO created = towerService.createTower(testTower);
        
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTowerCode()).isEqualTo("T001");
        assertThat(created.getTowerName()).isEqualTo("Test Tower");
    }

    @Test
    void testGetTowerById() {
        TransmissionTowerDTO created = towerService.createTower(testTower);
        
        Optional<TransmissionTowerDTO> retrieved = towerService.getTowerById(created.getId());
        
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTowerCode()).isEqualTo("T001");
    }

    @Test
    void testGetTowerByCode() {
        towerService.createTower(testTower);
        
        Optional<TransmissionTowerDTO> retrieved = towerService.getTowerByCode("T001");
        
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTowerName()).isEqualTo("Test Tower");
    }

    @Test
    void testUpdateTower() {
        TransmissionTowerDTO created = towerService.createTower(testTower);
        
        TransmissionTowerDTO updateDTO = new TransmissionTowerDTO();
        updateDTO.setTowerName("Updated Tower");
        updateDTO.setStatus("MAINTENANCE");
        
        TransmissionTowerDTO updated = towerService.updateTower(created.getId(), updateDTO);
        
        assertThat(updated.getTowerName()).isEqualTo("Updated Tower");
        assertThat(updated.getStatus()).isEqualTo("MAINTENANCE");
    }

    @Test
    void testDeleteTower() {
        TransmissionTowerDTO created = towerService.createTower(testTower);
        
        towerService.deleteTower(created.getId());
        
        Optional<TransmissionTowerDTO> retrieved = towerService.getTowerById(created.getId());
        assertThat(retrieved).isEmpty();
    }

}
