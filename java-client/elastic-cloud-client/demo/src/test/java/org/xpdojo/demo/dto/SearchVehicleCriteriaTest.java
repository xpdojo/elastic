package org.xpdojo.demo.dto;

import org.junit.jupiter.api.Test;
import org.xpdojo.aggregation.dto.SearchCriteria;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SearchVehicleCriteriaTest {

    @Test
    void test_constraint() {
        SearchVehicleRequestDto searchVehicleRequestDto = new SearchVehicleRequestDto();
        SearchVehicleCriteria searchVehicleCriteria = new SearchVehicleCriteria(searchVehicleRequestDto);
        Map<String, String> actual = searchVehicleCriteria.toTerms();

        assertThat(actual)
                .hasSize(12)
                .containsEntry("makers", "maker_code.keyword")
                .containsEntry("models", "model_code.keyword")
                .containsEntry("passengers", "passenger")
        ;
    }


    @Test
    void generate_search_criteria() {
        SearchCriteria searchCriteria = new SearchCriteria() {
            @Override
            public Map<String, String> toTerms() {
                return null;
            }

            @Override
            public Map<String, String> toCriteria() {
                Map<String, String> criteria = new HashMap<>();
                criteria.put("makers", "makers.keyword");
                criteria.put("models", "models.keyword");
                return criteria;
            }
        };

        Map<String, String> criteria = searchCriteria.toCriteria();
        assertThat(criteria).hasSize(2);
    }


}
