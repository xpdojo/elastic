package org.xpdojo.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class VehicleBoolQueryBuilder {

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

        addMustTermQuery(boolQueryBuilder, criteria);

        addMustQueryStringQuery(boolQueryBuilder, criteria);

        addRangeQuery(boolQueryBuilder, criteria);

        if (isNotBlank(criteria.get("external_company_code"))) {
            addMustNotQuery(boolQueryBuilder, criteria);
        }

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

        addMustTermQuery(boolQueryBuilder, criteria);

        addMustQueryStringQuery(boolQueryBuilder, criteria);

        addRangeQuery(boolQueryBuilder, criteria);

        if (isNotBlank(criteria.get("external_company_code"))) {
            addMustNotQuery(boolQueryBuilder, criteria);
        }

        return boolQueryBuilder;
    }

    /**
     * 단일 선택 필드
     *
     * @param boolQueryBuilder
     * @param criteria
     * @return
     */
    private static BoolQueryBuilder addMustTermQuery(BoolQueryBuilder boolQueryBuilder, Map<String, String> criteria) {

        final List<String> mustTermFields = Arrays.asList(
                "maker_code",
                "sub_model_code",
                "model_code",

                "is_deleted",
                "is_event",
                "has_media",
                "has_insurance_history",
                "is_guaranteed",
                "photographed_by_wini"
        );

        criteria.entrySet().stream()
                .filter(entry -> mustTermFields.contains(entry.getKey()))
                .forEach(entry -> boolQueryBuilder.must(termQuery(entry.getKey() + ".keyword", entry.getValue())));

        return boolQueryBuilder;
    }

    /**
     * 다중 선택 필드
     *
     * @param boolQueryBuilder
     * @param criteria
     * @return
     */
    private static BoolQueryBuilder addMustQueryStringQuery(BoolQueryBuilder boolQueryBuilder, Map<String, String> criteria) {

        final List<String> mustQueryStringFields = Arrays.asList(
                "transmission_code",
                "transmission_code",
                "location_code",
                "vehicle_type_code",
                "drive_type_code",
                "fuel_code",
                "exterior_color_code",
                "steering_code",
                "passenger",
                "condition_code",
                "options",

                "status_code"
        );

        criteria.entrySet().stream()
                .filter(entry -> mustQueryStringFields.contains(entry.getKey()))
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
     * @param criteria
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/7.17/common-options.html#date-math">Date Math</a>
     */
    private static BoolQueryBuilder addRangeQuery(BoolQueryBuilder boolQueryBuilder, Map<String, String> criteria) {

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

        return boolQueryBuilder;
    }

    /**
     * 제외 필드
     *
     * @param boolQueryBuilder
     * @param criteria
     * @return
     */
    private static BoolQueryBuilder addMustNotQuery(BoolQueryBuilder boolQueryBuilder, Map<String, String> criteria) {

        final List<String> mustNotExistsFields = Arrays.asList(
                "external_company_code"
        );

        criteria.entrySet().stream()
                .filter(entry -> mustNotExistsFields.contains(entry.getKey()))
                .forEach(entry ->
                        boolQueryBuilder
                                .mustNot(
                                        existsQuery(entry.getKey())
                                ));

        return boolQueryBuilder;
    }

}
