package org.xpdojo.elastic.vehicle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class CodeSet {
    List<Code> maker;
    List<Code> subModel;
    List<Code> steering;
    List<Code> fuel;
    List<Code> transmission;
    List<Code> condition;
    List<Code> drivetrain;
    List<Code> location;
    List<Code> color;
    List<Code> vehicleType;
}
