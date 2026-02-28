package pl.vdx.spaceshipfleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.vdx.spaceshipfleet.model.Spaceship;

public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
}
