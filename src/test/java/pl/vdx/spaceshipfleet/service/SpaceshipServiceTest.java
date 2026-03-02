package pl.vdx.spaceshipfleet.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.vdx.spaceshipfleet.dto.SpaceshipRequestDto;
import pl.vdx.spaceshipfleet.dto.SpaceshipResponseDto;
import pl.vdx.spaceshipfleet.exception.NotFoundException;
import pl.vdx.spaceshipfleet.model.Spaceship;
import pl.vdx.spaceshipfleet.repository.SpaceshipRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceshipServiceTest {

    @Mock
    private SpaceshipRepository repository;

    @InjectMocks
    private SpaceshipService service;

    @Test
    void getAll_withoutFilters_shouldCallFindAllAndMapToDto() {
        // given
        var pageable = PageRequest.of(0, 10);
        Spaceship ship = new Spaceship();
        ship.setId(1L);
        ship.setName("Falcon");
        Page<Spaceship> page = new PageImpl<>(List.of(ship), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(page);

        // when
        Page<SpaceshipResponseDto> result = service.getAll(null, null, pageable);

        // then
        verify(repository).findAll(pageable);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(0).name()).isEqualTo("Falcon");
    }

    @Test
    void getAll_withClassTypeFilter_shouldCallFindByClassTypeAndMapToDto() {
        // given
        String classType = "FIGHTER";
        var pageable = PageRequest.of(0, 5);

        Spaceship ship = new Spaceship();
        ship.setId(1L);
        ship.setName("Falcon");
        ship.setClassType(classType);
        ship.setHomeBase("Earth");

        Page<Spaceship> page = new PageImpl<>(List.of(ship), pageable, 1);

        when(repository.findByClassTypeContainingIgnoreCase(classType, pageable))
                .thenReturn(page);

        // when
        Page<SpaceshipResponseDto> result = service.getAll(classType, null, pageable);

        // then
        verify(repository).findByClassTypeContainingIgnoreCase(classType, pageable);
        assertThat(result.getTotalElements()).isEqualTo(1);
        SpaceshipResponseDto dto = result.getContent().get(0);
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Falcon");
        assertThat(dto.classType()).isEqualTo(classType);
    }


    @Test
    void create_shouldSaveEntityAndReturnDto() {
        // given
        SpaceshipRequestDto dto = new SpaceshipRequestDto();
        dto.setName("Falcon");
        dto.setClassType("FIGHTER");
        dto.setCrewCapacity(10);
        dto.setCurrentCrewCount(5);
        dto.setHomeBase("Earth");
        dto.setFuelLevelPercent(80.0);

        Spaceship toSave = new Spaceship();
        toSave.setName("Falcon");

        Spaceship saved = new Spaceship();
        saved.setId(1L);
        saved.setName("Falcon");
        saved.setClassType("FIGHTER");
        saved.setCrewCapacity(10);
        saved.setCurrentCrewCount(5);
        saved.setHomeBase("Earth");
        saved.setFuelLevelPercent(80.0);

        when(repository.save(any(Spaceship.class))).thenReturn(saved);

        // when
        SpaceshipResponseDto result = service.create(dto);

        // then
        verify(repository).save(any(Spaceship.class));
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Falcon");
        assertThat(result.classType()).isEqualTo("FIGHTER");
    }

    @Test
    void create_shouldThrowExceptionWhenCrewTooBig() {
        // given
        SpaceshipRequestDto dto = new SpaceshipRequestDto();
        dto.setName("Falcon");
        dto.setCrewCapacity(5);
        dto.setCurrentCrewCount(10);

        // when + then
        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void getById_shouldReturnDtoWhenFound() {
        // given
        Long id = 1L;
        Spaceship s = new Spaceship();
        s.setId(id);
        s.setName("Falcon");

        when(repository.findById(id)).thenReturn(Optional.of(s));

        // when
        SpaceshipResponseDto result = service.getById(id);

        // then
        verify(repository).findById(id);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Falcon");
    }

    @Test
    void getById_shouldThrowNotFoundWhenMissing() {
        // given
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class, () -> service.getById(id));
    }

    @Test
    void delete_shouldCallDeleteWhenExists() {
        // given
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // when
        service.delete(id);

        // then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_shouldThrowNotFoundWhenNotExists() {
        // given
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        // when + then
        assertThrows(NotFoundException.class, () -> service.delete(id));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void update_shouldUpdateEntityAndReturnDto() {
        // given
        Long id = 1L;
        Spaceship existing = new Spaceship();
        existing.setId(id);
        existing.setName("Old");

        SpaceshipRequestDto dto = new SpaceshipRequestDto();
        dto.setName("New");
        dto.setCrewCapacity(10);
        dto.setCurrentCrewCount(5);

        Spaceship saved = new Spaceship();
        saved.setId(id);
        saved.setName("New");
        saved.setCrewCapacity(10);
        saved.setCurrentCrewCount(5);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(saved);

        // when
        SpaceshipResponseDto result = service.update(id, dto);

        // then
        verify(repository).findById(id);
        verify(repository).save(existing);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("New");
    }

    @Test
    void update_shouldThrowNotFoundWhenMissing() {
        // given
        Long id = 1L;
        SpaceshipRequestDto dto = new SpaceshipRequestDto();
        dto.setName("New");

        when(repository.findById(id)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class, () -> service.update(id, dto));
        verify(repository, never()).save(any());
    }
}
