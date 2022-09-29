package org.xpdojo.demo.dto;

import lombok.Getter;
import lombok.ToString;
import org.xpdojo.search.criteria.SearchCriteria;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class SearchVehicleCriteria implements SearchCriteria {

    private String status_code;
    private String is_deleted;
    private String has_media;
    private String is_guaranteed;
    private String youtube_link;
    private String is_event;
    private String has_insurance_history;
    private String photographed_by_wini;

    private String has_fresh_stock_permission;
    private String is_fresh_stock;

    private String maker_code;
    private String sub_model_code;
    private String model_code;

    private String transmission_code;
    private String location_code;
    private String vehicle_type_code;
    private String drive_type_code;
    private String fuel_code;
    private String exterior_color_code;
    private String steering_code;

    private String product_price_from;
    private String product_price_to;
    private String engine_volume_from;
    private String engine_volume_to;
    private String model_year_from;
    private String model_year_to;
    private String passenger;

    private String offset;
    private String size;

    public SearchVehicleCriteria(SearchVehicleRequestDto searchVehicleRequestDto) {
        this.status_code = searchVehicleRequestDto.getStatus();
        this.is_deleted = searchVehicleRequestDto.getIsDeleted();
        this.has_media = searchVehicleRequestDto.getHasMedia();
        this.is_guaranteed = searchVehicleRequestDto.getIsGuaranteed();
        this.youtube_link = searchVehicleRequestDto.getHasYoutubeLink();
        this.is_event = searchVehicleRequestDto.getIsEvent();
        this.has_insurance_history = searchVehicleRequestDto.getHasInsuranceHistory();
        this.photographed_by_wini = searchVehicleRequestDto.getPhotographedByWini();

        this.has_fresh_stock_permission = searchVehicleRequestDto.getHasFreshStockPermission();
        this.is_fresh_stock = searchVehicleRequestDto.getIsFreshStock();

        this.maker_code = searchVehicleRequestDto.getMaker();
        this.sub_model_code = searchVehicleRequestDto.getSubModel();
        this.model_code = searchVehicleRequestDto.getModel();

        this.transmission_code = searchVehicleRequestDto.getTransmission();
        this.location_code = searchVehicleRequestDto.getLocation();
        this.vehicle_type_code = searchVehicleRequestDto.getVehicleType();
        this.drive_type_code = searchVehicleRequestDto.getDrivetrain();
        this.fuel_code = searchVehicleRequestDto.getFuel();
        this.exterior_color_code = searchVehicleRequestDto.getColor();
        this.steering_code = searchVehicleRequestDto.getSteering();

        this.product_price_from = searchVehicleRequestDto.getPriceFrom();
        this.product_price_to = searchVehicleRequestDto.getPriceTo();
        this.engine_volume_from = searchVehicleRequestDto.getEngineVolumeFrom();
        this.engine_volume_to = searchVehicleRequestDto.getEngineVolumeTo();
        this.model_year_from = searchVehicleRequestDto.getYearFrom();
        this.model_year_to = searchVehicleRequestDto.getYearTo();

        this.passenger = searchVehicleRequestDto.getPassenger();

        this.offset = searchVehicleRequestDto.getOffset();
        this.size = searchVehicleRequestDto.getSize();
    }

    /**
     * Option 카운팅 시 msearch 에서 사용
     *
     * @return 카운팅하는 Option Map
     * @see org.xpdojo.aggregation.application.CountOptionsService#aggregateVehicleOptions(SearchCriteria)
     */
    @Override
    public Map<String, String> toTerms() {
        Map<String, String> terms = new HashMap<>();
        // terms.put("status", "status_code.keyword");
        // terms.put("is_deleted", "is_deleted.keyword");
        // terms.put("has_media", "has_media.keyword");
        // terms.put("is_guaranteed", "is_guaranteed.keyword");
        // terms.put("youtube_link", "youtube_link.keyword");
        // terms.put("is_event", "is_event.keyword");
        // terms.put("has_insurance_history", "has_insurance_history.keyword");
        // terms.put("photographed_by_wini", "photographed_by_wini.keyword");

        terms.put("makers", "maker_code.keyword");
        terms.put("subModels", "sub_model_code.keyword");
        terms.put("models", "model_code.keyword");

        terms.put("transmissions", "transmission_code.keyword");
        terms.put("locations", "location_code.keyword");
        terms.put("vehicleTypes", "vehicle_type_code.keyword");
        terms.put("drivetrains", "drive_type_code.keyword");
        terms.put("fuels", "fuel_code.keyword");
        terms.put("colors", "exterior_color_code.keyword");
        // terms.put("steeringTypes", "steering_code.keyword");
        terms.put("steerings", "steering_code.keyword");

        // terms.put("product_price", "product_price");
        // terms.put("engine_volume", "engine_volume");
        // terms.put("model_year", "model_year");
        terms.put("passengers", "passenger");
        return terms;
    }
}
