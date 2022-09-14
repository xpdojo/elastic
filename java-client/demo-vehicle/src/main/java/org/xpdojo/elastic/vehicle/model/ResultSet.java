package org.xpdojo.elastic.vehicle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class ResultSet {
    List<Result> maker;
    List<Result> subModel;
    List<Result> steering;
    List<Result> fuel;
    List<Result> transmission;
    List<Result> condition;
    List<Result> drivetrain;
    List<Result> location;
    List<Result> color;
    // List<Result> option;
    List<Result> vehicleType;
}
