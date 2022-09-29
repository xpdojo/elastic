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
    @NullOrNotBlank private String drivetrain;
    @NullOrNotBlank private String fuel;
    @NullOrNotBlank private String colors;
    @NullOrNotBlank private String steering;

    @NullOrNotBlank private String priceFrom;
    @NullOrNotBlank private String priceTo;
    @NullOrNotBlank private String engineVolumeFrom;
    @NullOrNotBlank private String engineVolumeTo;
    @NullOrNotBlank private String yearFrom; // modelYearFrom
    @NullOrNotBlank private String yearTo; // modelYearTo
    @NullOrNotBlank private String passenger;

    @NullOrNotBlank private String offset;
    @NullOrNotBlank private String size;

}
