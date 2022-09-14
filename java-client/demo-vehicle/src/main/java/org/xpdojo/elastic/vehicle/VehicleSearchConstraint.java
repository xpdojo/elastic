package org.xpdojo.elastic.vehicle;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class VehicleSearchConstraint {

    private String maker;
    private String subModel;
    private String location;
    private String condition;
    private String vehicleType;
    private String steering;
    private String transmission;
    private String drivetrain;
    private String fuel;
    private String color;
    private String option;
    private String passenger;

}
