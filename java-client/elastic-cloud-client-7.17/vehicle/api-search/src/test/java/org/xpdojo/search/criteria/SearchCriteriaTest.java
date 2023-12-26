package org.xpdojo.search.criteria;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SearchCriteriaTest {

    @Test
    void generate_search_criteria() {
        SearchCriteria searchCriteria = new SearchCriteria() {
            @Override
            public Map<String, String> toTerms() {
                return null;
            }

            @Override
            public Map<String, String> toMap() {
                Map<String, String> criteria = new HashMap<>();
                criteria.put("makers", "makers.keyword");
                criteria.put("models", "models.keyword");
                return criteria;
            }
        };

        Map<String, String> criteria = searchCriteria.toMap();
        assertThat(criteria).hasSize(2);
    }

}
