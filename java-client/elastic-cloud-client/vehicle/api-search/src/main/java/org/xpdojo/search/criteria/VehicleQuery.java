package org.xpdojo.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class VehicleQuery {

    protected VehicleQuery() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 상품 검색 시 공통 쿼리 생성
     *
     * @param searchCriteria 검색 조건
     * @return 공통 쿼리
     */
    public static BoolQueryBuilder build(SearchVehicleCriteria searchCriteria) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Map<String, String> criteria = searchCriteria.toMap();

        if (criteria == null || criteria.isEmpty()) {
            return boolQueryBuilder.must(matchAllQuery());
        }

        addMustTermQuery(boolQueryBuilder, searchCriteria);

        addMustQueryStringQuery(boolQueryBuilder, searchCriteria);

        addRangeQuery(boolQueryBuilder, searchCriteria);

        if (isNotBlank(criteria.get("external_company_code"))) {
            addMustNotQuery(boolQueryBuilder, searchCriteria);
        }

        return boolQueryBuilder;
    }

    private static BoolQueryBuilder addMustTermQuery(BoolQueryBuilder boolQueryBuilder, SearchVehicleCriteria searchVehicleCriteria) {

        searchVehicleCriteria.toMap()
                .entrySet().stream()
                .filter(entry -> searchVehicleCriteria.listMustTermFields().contains(entry.getKey()))
                .forEach(entry -> boolQueryBuilder.must(termQuery(entry.getKey() + ".keyword", entry.getValue())));

        return boolQueryBuilder;
    }

    private static BoolQueryBuilder addMustQueryStringQuery(BoolQueryBuilder boolQueryBuilder, SearchVehicleCriteria searchVehicleCriteria) {

        searchVehicleCriteria.toMap()
                .entrySet().stream()
                .filter(entry -> searchVehicleCriteria.listMustQueryStringFields().contains(entry.getKey()))
                .forEach(entry ->
                        boolQueryBuilder
                                .must(
                                        queryStringQuery(String.join(" OR ", entry.getValue().split(",")))
                                                .field(entry.getKey())));

        return boolQueryBuilder;
    }

    /**
     * 범위 필드
     *
     * @param boolQueryBuilder
     * @param searchVehicleCriteria
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/7.17/common-options.html#date-math">Date Math</a>
     */
    private static BoolQueryBuilder addRangeQuery(BoolQueryBuilder boolQueryBuilder, SearchVehicleCriteria searchVehicleCriteria) {

        Map<String, String> criteria = searchVehicleCriteria.toMap();

        if (isNotBlank(criteria.get("has_fresh_stock_permission"))
                && isNotBlank(criteria.get("is_fresh_stock"))) {
            boolQueryBuilder.must(
                    rangeQuery("@timestamp")
                            .from("now-1d/s"));
        } else if (isBlank(criteria.get("has_fresh_stock_permission"))) {
            boolQueryBuilder.must(
                    rangeQuery("@timestamp")
                            .to("now-1d/s"));
        }

        searchVehicleCriteria
                .listMustRangeFields().stream()
                .filter(field -> isNotBlank(criteria.get(field + "_from")) || isNotBlank(criteria.get(field + "_to")))
                .forEach(field -> boolQueryBuilder
                        .must(
                                rangeQuery(field)
                                        .from(criteria.get(field + "_from"))
                                        .to(criteria.get(field + "_to"))));

        return boolQueryBuilder;
    }

    private static BoolQueryBuilder addMustNotQuery(BoolQueryBuilder boolQueryBuilder, SearchVehicleCriteria searchVehicleCriteria) {

        searchVehicleCriteria.toMap()
                .entrySet().stream()
                .filter(entry -> searchVehicleCriteria.listMustNotExistsFields().contains(entry.getKey()))
                .forEach(entry ->
                        boolQueryBuilder
                                .mustNot(
                                        existsQuery(entry.getKey())
                                ));

        return boolQueryBuilder;
    }

}
