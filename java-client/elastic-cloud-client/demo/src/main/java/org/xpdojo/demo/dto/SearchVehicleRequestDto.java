package org.xpdojo.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xpdojo.demo.NullOrNotBlank;

@Getter
@Setter
@ToString
public class SearchVehicleRequestDto {

    @NullOrNotBlank private String status = "C030";
    @NullOrNotBlank private String isDeleted = "N";
    @NullOrNotBlank private String hasMedia;
    @NullOrNotBlank private String isGuaranteed;
    @NullOrNotBlank private String hasYoutubeLink;
    @NullOrNotBlank private String isEvent;
    @NullOrNotBlank private String hasInsuranceHistory;
    @NullOrNotBlank private String isFreshStock;
    @NullOrNotBlank private String photographedByWini;

    @NullOrNotBlank private String maker;
    @NullOrNotBlank private String subModel;
    @NullOrNotBlank private String model;

    @NullOrNotBlank private String transmission;
    @NullOrNotBlank private String location;
    @NullOrNotBlank private String vehicleType;
    @NullOrNotBlank private String driveType;
    @NullOrNotBlank private String fuel;
    @NullOrNotBlank private String colors;
    @NullOrNotBlank private String steering;

    @NullOrNotBlank private String price;
    @NullOrNotBlank private String engineVolume;
    @NullOrNotBlank private String modelYear;
    @NullOrNotBlank private String passenger;

}
