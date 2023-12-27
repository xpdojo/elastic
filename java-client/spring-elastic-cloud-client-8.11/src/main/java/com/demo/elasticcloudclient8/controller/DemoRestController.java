package com.demo.elasticcloudclient8.controller;

import com.demo.elasticcloudclient8.domain.Car;
import com.demo.elasticcloudclient8.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DemoRestController {

    private final CarService carService;

    /**
     * http localhost:23345
     */
    @GetMapping
    public List<Car> listVehicles(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return carService.findAll(offset, size);
    }

    /**
     * http localhost:23345/price\?priceFrom=30_000\&size=1
     */
    @GetMapping("/price")
    public List<Car> listVehiclesByPrice(
            @RequestParam(value = "priceFrom", defaultValue = "0") Integer priceFrom,
            @RequestParam(value = "priceTo", defaultValue = "3000000") Integer priceTo,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return carService.findByPrice(priceFrom, priceTo, offset, size);
    }


    /**
     * http localhost:23345/keyword\?q=BMW\&size=1
     */
    @GetMapping("/keyword")
    public List<Car> listVehiclesByKeyword(
            @RequestParam(value = "q") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return carService.findByKeyword(keyword, offset, size);
    }

}
