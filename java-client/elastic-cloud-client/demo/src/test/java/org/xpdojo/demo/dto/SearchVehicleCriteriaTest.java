package org.xpdojo.demo.dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SearchVehicleCriteriaTest {

    @Test
    void test_constraint() {
        SearchVehicleRequestDto searchVehicleRequestDto = new SearchVehicleRequestDto();
        SearchVehicleCriteria searchVehicleCriteria = new SearchVehicleCriteria(searchVehicleRequestDto);
        Map<String, String> actual = searchVehicleCriteria.toTerms();

        assertThat(actual)
                .containsEntry("makers", "maker_code.keyword")
                .containsEntry("models", "model_code.keyword")
                .containsEntry("passengers", "passenger")
        ;
    }

}
