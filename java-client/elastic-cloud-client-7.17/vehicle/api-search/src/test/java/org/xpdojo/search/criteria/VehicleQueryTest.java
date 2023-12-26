package org.xpdojo.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
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
                .hasAtLeastOneElementOfType(TermQueryBuilder.class)
                .hasAtLeastOneElementOfType(QueryStringQueryBuilder.class)
                .hasAtLeastOneElementOfType(RangeQueryBuilder.class)
        ;
    }

    @Test
    void build_query_with_term_parameter() {
        SearchVehicleRequestDto searchVehicleRequestDto = new SearchVehicleRequestDto();
        searchVehicleRequestDto.setLocation("test-location");

        SearchVehicleCriteria searchVehicleCriteria = new SearchVehicleCriteria(searchVehicleRequestDto);
        BoolQueryBuilder boolQueryBuilder = VehicleQuery.build(searchVehicleCriteria);

        assertThat(boolQueryBuilder.hasClauses()).isTrue();

        assertThat(boolQueryBuilder.must())
                .hasSize(4)
                .hasAtLeastOneElementOfType(TermQueryBuilder.class)
                .hasAtLeastOneElementOfType(QueryStringQueryBuilder.class)
                .hasAtLeastOneElementOfType(RangeQueryBuilder.class)
        ;
    }

    @Test
    void build_query_with_must_not_parameter() {
        SearchVehicleRequestDto searchVehicleRequestDto = new SearchVehicleRequestDto();
        searchVehicleRequestDto.setInKorea("yes");

        SearchVehicleCriteria searchVehicleCriteria = new SearchVehicleCriteria(searchVehicleRequestDto);
        BoolQueryBuilder boolQueryBuilder = VehicleQuery.build(searchVehicleCriteria);

        assertThat(boolQueryBuilder.hasClauses()).isTrue();

        assertThat(boolQueryBuilder.mustNot())
                .hasSize(1)
                .hasExactlyElementsOfTypes(ExistsQueryBuilder.class)
        ;
    }

}
