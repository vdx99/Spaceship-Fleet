package pl.vdx.spaceshipfleet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.vdx.spaceshipfleet.dto.SpaceshipRequestDto;
import pl.vdx.spaceshipfleet.dto.SpaceshipResponseDto;
import pl.vdx.spaceshipfleet.service.RequestCounterService;
import pl.vdx.spaceshipfleet.service.SpaceshipService;

import java.util.List;

@RestController
@RequestMapping("/api/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

    private final SpaceshipService service;
    private final RequestCounterService counterService;

    @GetMapping
    public List<SpaceshipResponseDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<SpaceshipResponseDto> create(@Valid @RequestBody SpaceshipRequestDto dto) {
        SpaceshipResponseDto created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/metrics/requests")
    public long getRequestCount() {
        return counterService.getCount();
    }
}
