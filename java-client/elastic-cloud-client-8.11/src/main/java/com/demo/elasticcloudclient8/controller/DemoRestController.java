package com.demo.elasticcloudclient8.controller;

import com.demo.elasticcloudclient8.domain.Car;
import com.demo.elasticcloudclient8.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoRestController {

    private final CarService carService;

    /**
     * http localhost:23000
     */
    @GetMapping
    public Iterable<Car> listVehicles(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return carService.findAll(offset, size);
    }

}
