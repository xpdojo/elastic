package org.xpdojo.elastic.vehicle.model;

public enum Field {

    MAKER("PC0008", "make_cd"),
    SUB_MODEL("PC0294", "model_cd"),
    // MODEL("PC0000", "dtl_model_cd"),
    LOCATION("PC0003", "location_cd"),
    CONDITION("PC0006", "type_cd"),
    VEHICLE_TYPE("PC0007", "vehicle_type"),
    STEERING("PC0009", "steering_cd"),
    TRANSMISSION("PC0010", "transmission_cd"),
    DRIVETRAIN("PC0011", "drivetrain_cd"),
    FUEL("PC0012", "fuel_type_cd"),
    COLOR("PC0015", "exterior_color_cd"),
    OPTION("PC0021", "option_info.option_cd"),
    PASSENGER("passenger", "passenger");

    private String code;
    private String name;

    Field(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
