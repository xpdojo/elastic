package org.xpdojo.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpdojo.search.domain.Car;
import org.xpdojo.search.application.CarService;

@RestController
public class DemoRestController {

    private final CarService carService;

    public DemoRestController(CarService carService) {
        this.carService = carService;
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

}
