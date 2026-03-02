package pl.vdx.spaceshipfleet.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.vdx.spaceshipfleet.model.Spaceship;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpaceshipRepositoryTest {

    @Autowired
    private SpaceshipRepository repository;

    @Test
    void findByClassTypeContainingIgnoreCase_shouldReturnMatchingShips() {
        // given
        Spaceship s1 = new Spaceship();
        s1.setName("Falcon");
        s1.setClassType("FIGHTER");
        s1.setCrewCapacity(10);
        s1.setCurrentCrewCount(5);
        s1.setHomeBase("Earth");
        s1.setFuelLevelPercent(80.0);

        Spaceship s2 = new Spaceship();
        s2.setName("Cargo-1");
        s2.setClassType("FREIGHTER");
        s2.setCrewCapacity(3);
        s2.setCurrentCrewCount(2);
        s2.setHomeBase("Mars");
        s2.setFuelLevelPercent(60.0);

        repository.save(s1);
        repository.save(s2);

        var pageable = PageRequest.of(0, 10);

        // when
        Page<Spaceship> result =
                repository.findByClassTypeContainingIgnoreCase("fight", pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        Spaceship found = result.getContent().get(0);
        assertThat(found.getName()).isEqualTo("Falcon");
        assertThat(found.getClassType()).isEqualTo("FIGHTER");
    }
}
