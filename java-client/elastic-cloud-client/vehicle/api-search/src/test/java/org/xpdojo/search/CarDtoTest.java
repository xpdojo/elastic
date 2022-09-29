package org.xpdojo.search;

import org.junit.jupiter.api.Test;
import org.xpdojo.search.domain.CarDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarDtoTest {

    @Test
    void test_car_dto() {

        CarDto actual = CarDto.builder()
                .id("1")
                .build();

        String expected = "1";

        assertEquals(expected, actual.getId());
    }
}
