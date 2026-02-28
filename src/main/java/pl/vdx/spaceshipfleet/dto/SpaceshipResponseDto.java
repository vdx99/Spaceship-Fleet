package pl.vdx.spaceshipfleet.dto;

public record SpaceshipResponseDto(
        Long id,
        String name,
        String classType,
        Integer crewCapacity,
        Integer currentCrewCount,
        String homeBase,
        Double fuelLevelPercent
) {}
