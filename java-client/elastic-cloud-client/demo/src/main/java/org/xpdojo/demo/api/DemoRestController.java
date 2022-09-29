package org.xpdojo.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpdojo.aggregation.application.CountOptionsService;
import org.xpdojo.aggregation.dto.Option;
import org.xpdojo.demo.dto.SearchVehicleCriteria;
import org.xpdojo.demo.dto.SearchVehicleRequestDto;
import org.xpdojo.search.application.CarService;
import org.xpdojo.search.domain.Car;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class DemoRestController {

    private final CarService carService;
    private final CountOptionsService countOptionsService;

    public DemoRestController(CarService carService, CountOptionsService countOptionsService) {
        this.carService = carService;
        this.countOptionsService = countOptionsService;
    }

    @GetMapping("/")
    public String root() {
        return "demo";
    }

    @GetMapping("/all")
    public Iterable<Car> listVehicles(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return carService.findAll(offset, size);
    }

    @GetMapping("/count")
    public long countVehicles(
            @ModelAttribute @Valid SearchVehicleRequestDto searchVehicleRequestDto) {
        return carService.count(new SearchVehicleCriteria(searchVehicleRequestDto));
    }

    @GetMapping("/vehicles")
    public Iterable<Car> listVehicles(
            @ModelAttribute @Valid SearchVehicleRequestDto searchVehicleRequestDto) {
        return carService.search(new SearchVehicleCriteria(searchVehicleRequestDto));
    }

    @GetMapping("/options")
    public Map<String, List<Option>> countOptions(
            @ModelAttribute @Valid SearchVehicleRequestDto searchVehicleRequestDto) {
        return countOptionsService.aggregateVehicleOptions(new SearchVehicleCriteria(searchVehicleRequestDto));
    }

}
