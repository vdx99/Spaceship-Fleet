package pl.vdx.spaceshipfleet.dto;

import jakarta.validation.constraints.*;

public class SpaceshipRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String classType;

    @Min(1)
    private Integer crewCapacity;

    @PositiveOrZero
    private Integer currentCrewCount;

    private String homeBase;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double fuelLevelPercent;

    // gettery/settery
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public Integer getCrewCapacity() { return crewCapacity; }
    public void setCrewCapacity(Integer crewCapacity) { this.crewCapacity = crewCapacity; }
    public Integer getCurrentCrewCount() { return currentCrewCount; }
    public void setCurrentCrewCount(Integer currentCrewCount) { this.currentCrewCount = currentCrewCount; }
    public String getHomeBase() { return homeBase; }
    public void setHomeBase(String homeBase) { this.homeBase = homeBase; }
    public Double getFuelLevelPercent() { return fuelLevelPercent; }
    public void setFuelLevelPercent(Double fuelLevelPercent) { this.fuelLevelPercent = fuelLevelPercent; }
}
