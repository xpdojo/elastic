package org.xpdojo.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleQueryTest {

    @Test
    void build_query_without_parameter() {
        SearchVehicleCriteria searchVehicleCriteria = new SearchVehicleCriteria(new SearchVehicleRequestDto());
        BoolQueryBuilder boolQueryBuilder = VehicleQuery.build(searchVehicleCriteria);

        assertThat(boolQueryBuilder.hasClauses()).isTrue();

        List<QueryBuilder> must = boolQueryBuilder.must();
        assertThat(must)
                .hasSize(3)
                .hasExactlyElementsOfTypes(
                        TermQueryBuilder.class,
                        QueryStringQueryBuilder.class,
                        RangeQueryBuilder.class
                );
    }

    @Test
    void build_query_with_parameter() {
        SearchVehicleRequestDto searchVehicleRequestDto = new SearchVehicleRequestDto();
        searchVehicleRequestDto.setLocation("test-location");

        SearchVehicleCriteria searchVehicleCriteria = new SearchVehicleCriteria(searchVehicleRequestDto);
        BoolQueryBuilder boolQueryBuilder = VehicleQuery.build(searchVehicleCriteria);

        assertThat(boolQueryBuilder.hasClauses()).isTrue();

        List<QueryBuilder> must = boolQueryBuilder.must();
        assertThat(must)
                .hasSize(4)
                .hasExactlyElementsOfTypes(
                        TermQueryBuilder.class,
                        QueryStringQueryBuilder.class,
                        QueryStringQueryBuilder.class,
                        RangeQueryBuilder.class
                );
    }

}
