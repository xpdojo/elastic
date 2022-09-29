package org.xpdojo.aggregation.application;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.xpdojo.aggregation.dto.Option;
import org.xpdojo.search.criteria.SearchCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CountOptionsServiceTest {

    private CountOptionsService countOptionsService;

    @MockBean
    private RestHighLevelClient restHighLevelClient;

    @BeforeEach
    void setUp() {
        countOptionsService = new CountOptionsService(restHighLevelClient);

        ReflectionTestUtils.setField(countOptionsService, "INDEX", "for-test");
    }

    @Test
    void generate_multiSearchRequest() {
        SearchCriteria searchCriteria = new SearchCriteria() {
            @Override
            public Map<String, String> toTerms() {
                Map<String, String> terms = new HashMap<>();
                terms.put("makers", "makers.keyword");
                terms.put("models", "models.keyword");
                return terms;
            }

            @Override
            public Map<String, String> toCriteria() {
                Map<String, String> criteria = new HashMap<>();
                criteria.put("makers", "maker1");
                criteria.put("models", "model2");
                criteria.put("colors", "black");
                return criteria;
            }
        };

        Map<String, String> criteria = searchCriteria.toCriteria();
        assertThat(criteria).hasSize(3);

        MultiSearchRequest multiSearchRequest = countOptionsService.generateMultiSearchRequest(searchCriteria);
        assertThat(multiSearchRequest.requests()).hasSize(2);
    }

    @Test
    void should_return_empty_multiSearchResponse_items() {
        MultiSearchResponse.Item[] items = new MultiSearchResponse.Item[1];

        MultiSearchResponse multiSearchResponse = new MultiSearchResponse(items, 300);
        Map<String, List<Option>> optionAggregations = countOptionsService.extractOptionAggregations(multiSearchResponse);

        assertThat(optionAggregations).isEmpty();
    }

    @Test
    void should_return_empty_multiSearchResponse_item_searchResponse() {
        MultiSearchResponse.Item[] items = new MultiSearchResponse.Item[1];
        MultiSearchResponse.Item item = new MultiSearchResponse.Item(null, null);
        items[0] = item;

        MultiSearchResponse multiSearchResponse = new MultiSearchResponse(items, 300);
        Map<String, List<Option>> optionAggregations = countOptionsService.extractOptionAggregations(multiSearchResponse);

        assertThat(optionAggregations).isEmpty();
    }

}
