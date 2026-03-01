package pl.vdx.spaceshipfleet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.vdx.spaceshipfleet.dto.SpaceshipRequestDto;
import pl.vdx.spaceshipfleet.dto.SpaceshipResponseDto;
import pl.vdx.spaceshipfleet.service.RequestCounterService;
import pl.vdx.spaceshipfleet.service.SpaceshipService;

@RestController
@RequestMapping("/api/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

    private final SpaceshipService service;
    private final RequestCounterService counterService;

    @GetMapping
    public Page<SpaceshipResponseDto> getAll(
            @RequestParam(required = false) String classType,
            @RequestParam(required = false) String homeBase,
            @PageableDefault(size = 10) Pageable pageable) {
        return service.getAll(classType, homeBase, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceshipResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<SpaceshipResponseDto> create(@Valid @RequestBody SpaceshipRequestDto dto) {
        SpaceshipResponseDto created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceshipResponseDto> update(@PathVariable Long id,
                                                       @Valid @RequestBody SpaceshipRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metrics/requests")
    public long getRequestCount() {
        return counterService.getCount();
    }
}
