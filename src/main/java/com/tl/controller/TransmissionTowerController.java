package com.tl.controller;

import com.tl.domain.dto.TransmissionTowerDTO;
import com.tl.service.TransmissionTowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/towers")
@Tag(name = "输电塔管理", description = "输电塔信息查询和管理")
@RequiredArgsConstructor
public class TransmissionTowerController {

    private final TransmissionTowerService service;

    @PostMapping
    @Operation(summary = "创建输电塔")
    public ResponseEntity<TransmissionTowerDTO> create(@RequestBody TransmissionTowerDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新输电塔")
    public ResponseEntity<TransmissionTowerDTO> update(
            @PathVariable Long id,
            @RequestBody TransmissionTowerDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询输电塔")
    public ResponseEntity<TransmissionTowerDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据塔号查询输电塔")
    public ResponseEntity<TransmissionTowerDTO> getByCode(@PathVariable String code) {
        return service.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "分页查询输电塔")
    public ResponseEntity<Page<TransmissionTowerDTO>> list(Pageable pageable) {
        return ResponseEntity.ok(service.findPage(pageable));
    }

    @GetMapping("/line/{lineId}")
    @Operation(summary = "根据线路查询输电塔")
    public ResponseEntity<Page<TransmissionTowerDTO>> getByLineId(
            @PathVariable Long lineId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByLineId(lineId, pageable));
    }

    @GetMapping("/nearby")
    @Operation(summary = "查询附近的输电塔")
    public ResponseEntity<Page<TransmissionTowerDTO>> getNearby(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam(defaultValue = "5") Double radiusKm,
            Pageable pageable) {
        return ResponseEntity.ok(service.findNearby(longitude, latitude, radiusKm, pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除输电塔")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}