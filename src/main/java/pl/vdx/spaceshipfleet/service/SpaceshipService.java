package pl.vdx.spaceshipfleet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.vdx.spaceshipfleet.dto.SpaceshipRequestDto;
import pl.vdx.spaceshipfleet.dto.SpaceshipResponseDto;
import pl.vdx.spaceshipfleet.exception.NotFoundException;
import pl.vdx.spaceshipfleet.model.Spaceship;
import pl.vdx.spaceshipfleet.repository.SpaceshipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceshipService {

    private final SpaceshipRepository repository;

    public Page<SpaceshipResponseDto> getAll(String classType,
                                             String homeBase,
                                             Pageable pageable) {
        Page<Spaceship> page;

        if (classType != null && !classType.isBlank() &&
                homeBase != null && !homeBase.isBlank()) {
            page = repository.findByClassTypeContainingIgnoreCaseAndHomeBaseContainingIgnoreCase(
                    classType, homeBase, pageable);
        } else if (classType != null && !classType.isBlank()) {
            page = repository.findByClassTypeContainingIgnoreCase(classType, pageable);
        } else if (homeBase != null && !homeBase.isBlank()) {
            page = repository.findByHomeBaseContainingIgnoreCase(homeBase, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    public SpaceshipResponseDto create(SpaceshipRequestDto dto) {
        Spaceship spaceship = new Spaceship();
        updateEntityFromDto(spaceship, dto);
        validateCrew(spaceship);
        Spaceship saved = repository.save(spaceship);
        return toDto(saved);
    }

    private void updateEntityFromDto(Spaceship s, SpaceshipRequestDto dto) {
        s.setName(dto.getName());
        s.setClassType(dto.getClassType());
        s.setCrewCapacity(dto.getCrewCapacity());
        s.setCurrentCrewCount(dto.getCurrentCrewCount());
        s.setHomeBase(dto.getHomeBase());
        s.setFuelLevelPercent(dto.getFuelLevelPercent());
    }

    private void validateCrew(Spaceship s) {
        if (s.getCrewCapacity() != null && s.getCurrentCrewCount() != null
                && s.getCurrentCrewCount() > s.getCrewCapacity()) {
            throw new IllegalArgumentException("currentCrewCount cannot be greater than crewCapacity");
        }
    }

    private SpaceshipResponseDto toDto(Spaceship s) {
        return new SpaceshipResponseDto(
                s.getId(),
                s.getName(),
                s.getClassType(),
                s.getCrewCapacity(),
                s.getCurrentCrewCount(),
                s.getHomeBase(),
                s.getFuelLevelPercent()
        );
    }

    public SpaceshipResponseDto getById(Long id) {
        Spaceship s = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Spaceship not found: " + id));
        return toDto(s);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Spaceship not found: " + id);
        }
        repository.deleteById(id);
    }

    public SpaceshipResponseDto update(Long id, SpaceshipRequestDto dto) {
        Spaceship s = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Spaceship not found: " + id));
        updateEntityFromDto(s, dto);
        validateCrew(s);
        Spaceship saved = repository.save(s);
        return toDto(saved);
    }

}
