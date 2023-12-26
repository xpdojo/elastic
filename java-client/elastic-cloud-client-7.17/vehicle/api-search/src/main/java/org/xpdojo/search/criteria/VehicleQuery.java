package org.xpdojo.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
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

        String freshStockCondition = "now-1d/s";
        if (isNotBlank(criteria.get("has_fresh_stock_permission"))
                && isNotBlank(criteria.get("is_fresh_stock"))) {
            boolQueryBuilder.must(
                    rangeQuery("@timestamp")
                            .from(freshStockCondition));
        } else if (isBlank(criteria.get("has_fresh_stock_permission"))) {
            boolQueryBuilder.must(
                    rangeQuery("@timestamp")
                            .to(freshStockCondition));
        }

        listTermQuery(searchCriteria).forEach(boolQueryBuilder::must);

        listQueryStringQuery(searchCriteria).forEach(boolQueryBuilder::must);

        listRangeQuery(searchCriteria).forEach(boolQueryBuilder::must);

        if (isNotBlank(criteria.get("external_company_code"))) {
            listExistsQuery(searchCriteria).forEach(boolQueryBuilder::mustNot);
        }

        return boolQueryBuilder;
    }

    private static List<TermQueryBuilder> listTermQuery(SearchVehicleCriteria searchVehicleCriteria) {
        return searchVehicleCriteria.toMap()
                .entrySet().stream()
                .filter(entry -> searchVehicleCriteria.listMustTermFields().contains(entry.getKey()))
                .map(entry -> termQuery(entry.getKey() + ".keyword", entry.getValue()))
                .collect(Collectors.toList());
    }

    private static List<QueryStringQueryBuilder> listQueryStringQuery(SearchVehicleCriteria searchVehicleCriteria) {
        return searchVehicleCriteria.toMap()
                .entrySet().stream()
                .filter(entry -> searchVehicleCriteria.listMustQueryStringFields().contains(entry.getKey()))
                .map(entry -> queryStringQuery(String.join(" OR ", entry.getValue().split(","))).field(entry.getKey()))
                // .collect(Collectors.collectingAndThen(Collectors.toList(), QueryBuilders::boolQuery));
                .collect(Collectors.toList());
    }

    private static List<RangeQueryBuilder> listRangeQuery(SearchVehicleCriteria searchVehicleCriteria) {
        Map<String, String> criteria = searchVehicleCriteria.toMap();

        return searchVehicleCriteria
                .listMustRangeFields().stream()
                .filter(field -> isNotBlank(criteria.get(field + "_from")) || isNotBlank(criteria.get(field + "_to")))
                .map(field -> rangeQuery(field)
                                        .from(criteria.get(field + "_from"))
                                        .to(criteria.get(field + "_to")))
                .collect(Collectors.toList());
    }

    private static List<ExistsQueryBuilder> listExistsQuery(SearchVehicleCriteria searchVehicleCriteria) {
        return searchVehicleCriteria.toMap()
                .keySet().stream()
                .filter(s -> searchVehicleCriteria.listMustNotExistsFields().contains(s))
                .map(QueryBuilders::existsQuery)
                .collect(Collectors.toList());
    }

}
