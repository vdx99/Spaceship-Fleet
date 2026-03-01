package pl.vdx.spaceshipfleet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.vdx.spaceshipfleet.model.Spaceship;

public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
    Page<Spaceship> findByClassTypeContainingIgnoreCase(String classType, Pageable pageable);

    Page<Spaceship> findByHomeBaseContainingIgnoreCase(String homeBase, Pageable pageable);

    Page<Spaceship> findByClassTypeContainingIgnoreCaseAndHomeBaseContainingIgnoreCase(
            String classType, String homeBase, Pageable pageable
    );
}
