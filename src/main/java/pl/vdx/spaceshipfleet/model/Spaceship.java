package pl.vdx.spaceshipfleet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "spaceships")
@Getter
@Setter
public class Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String classType; // np. FREIGHTER, FIGHTER

    private Integer crewCapacity;

    private Integer currentCrewCount;

    private String homeBase;

    private Double fuelLevelPercent;
}
