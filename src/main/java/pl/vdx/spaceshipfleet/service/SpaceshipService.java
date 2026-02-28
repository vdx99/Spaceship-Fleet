package pl.vdx.spaceshipfleet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.vdx.spaceshipfleet.dto.SpaceshipRequestDto;
import pl.vdx.spaceshipfleet.dto.SpaceshipResponseDto;
import pl.vdx.spaceshipfleet.model.Spaceship;
import pl.vdx.spaceshipfleet.repository.SpaceshipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceshipService {

    private final SpaceshipRepository repository;

    public List<SpaceshipResponseDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
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
}
