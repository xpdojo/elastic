package org.xpdojo.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

public class VehicleBoolQueryBuilder {

    private static List<String> notKeywordFields = Arrays.asList(
            "offset", "size",
            "has_fresh_stock_permission", "is_fresh_stock",
            "product_price_from", "product_price_to",
            "engine_volume_from", "engine_volume_to",
            "model_year_from", "model_year_to"
    );

    protected VehicleBoolQueryBuilder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 상품 검색 시 공통 쿼리 생성
     *
     * @param criteria 검색 조건
     * @return 공통 쿼리
     */
    public static BoolQueryBuilder generateVehicleQuery(Map<String, String> criteria) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (criteria == null || criteria.isEmpty()) {
            return boolQueryBuilder.must(matchAllQuery());
        }

        criteria.entrySet().stream()
                .filter(entry -> !notKeywordFields.contains(entry.getKey()))
                // .forEach(entry -> boolQueryBuilder.must(termQuery(entry.getKey() + ".keyword", entry.getValue())));
                .forEach(entry ->
                        boolQueryBuilder
                                .must(
                                        queryStringQuery(String.join(" OR ", entry.getValue().split(",")))
                                                .field(entry.getKey())));

        addRangeQuery(boolQueryBuilder, criteria);

        return boolQueryBuilder;
    }

    /**
     * 옵션 카운팅
     *
     * @param keywordTerm
     * @param criteria
     * @return
     */
    public static BoolQueryBuilder generateOptionsQuery(String keywordTerm, Map<String, String> criteria) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (criteria == null || criteria.isEmpty()) {
            return boolQueryBuilder.must(matchAllQuery());
        }

        criteria.entrySet().stream()
                .filter(entry -> !notKeywordFields.contains(entry.getKey()))
                .filter(entry -> !entry.getKey().equals(keywordTerm.replace(".keyword", "")))
                // .forEach(entry -> boolQueryBuilder.must(termQuery(entry.getKey() + ".keyword", entry.getValue())));
                .forEach(entry ->
                        boolQueryBuilder
                                .must(
                                        queryStringQuery(String.join(" OR ", entry.getValue().split(",")))
                                                .field(entry.getKey())));

        addRangeQuery(boolQueryBuilder, criteria);

        return boolQueryBuilder;
    }

    private static void addRangeQuery(
            BoolQueryBuilder boolQueryBuilder,
            Map<String, String> criteria) {

        // https://www.elastic.co/guide/en/elasticsearch/reference/7.17/common-options.html#date-math
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

        if (isNotBlank(criteria.get("product_price_from"))
                || isNotBlank(criteria.get("product_price_to"))) {
            boolQueryBuilder.must(
                    rangeQuery("product_price")
                            .from(criteria.get("product_price_from"))
                            .to(criteria.get("product_price_to")));
        }

        if (isNotBlank(criteria.get("engine_volume_from"))
                || isNotBlank(criteria.get("engine_volume_to"))) {
            boolQueryBuilder.must(
                    rangeQuery("engine_volume")
                            .from(criteria.get("engine_volume_from"))
                            .to(criteria.get("engine_volume_to")));
        }

        if (isNotBlank(criteria.get("model_year_from"))
                || isNotBlank(criteria.get("model_year_to"))) {
            boolQueryBuilder.must(
                    rangeQuery("model_year")
                            .from(criteria.get("model_year_from"))
                            .to(criteria.get("model_year_to")));
        }
    }

}
