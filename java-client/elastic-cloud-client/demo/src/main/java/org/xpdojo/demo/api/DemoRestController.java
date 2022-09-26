package org.xpdojo.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpdojo.aggregation.application.CountOptionsService;
import org.xpdojo.aggregation.dto.Constraint;
import org.xpdojo.search.application.CarService;
import org.xpdojo.search.domain.Car;

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

    @GetMapping("/vehicles")
    public Iterable<Car> listVehicles(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "status", defaultValue = "C030") String status
    ) {
        return carService.search(offset, size, status);
    }

    @GetMapping("/count")
    public Constraint countOptions(
            @RequestParam(value = "status", defaultValue = "C030") String status
    ) {
        return countOptionsService.aggregations(status);
    }

}
