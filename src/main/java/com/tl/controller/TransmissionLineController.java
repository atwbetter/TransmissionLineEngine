package com.tl.controller;

import com.tl.domain.dto.TransmissionLineDTO;
import com.tl.service.TransmissionLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines")
@Tag(name = "输电线路管理", description = "输电线路信息查询和管理")
@RequiredArgsConstructor
public class TransmissionLineController {

    private final TransmissionLineService service;

    @PostMapping
    @Operation(summary = "创建输电线路")
    public ResponseEntity<TransmissionLineDTO> create(@RequestBody TransmissionLineDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新输电线路")
    public ResponseEntity<TransmissionLineDTO> update(
            @PathVariable Long id,
            @RequestBody TransmissionLineDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询输电线路")
    public ResponseEntity<TransmissionLineDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据线路代码查询")
    public ResponseEntity<TransmissionLineDTO> getByCode(@PathVariable String code) {
        return service.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "分页查询输电线路")
    public ResponseEntity<Page<TransmissionLineDTO>> list(Pageable pageable) {
        return ResponseEntity.ok(service.findPage(pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除输电线路")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}