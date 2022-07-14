package org.xpdojo.elastic.vehicle;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xpdojo.elastic.vehicle.model.Field;
import org.xpdojo.elastic.vehicle.model.Result;
import org.xpdojo.elastic.vehicle.model.ResultSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    private final RestHighLevelClient rhlc;

    public SearchService(RestHighLevelClient rhlc) {
        this.rhlc = rhlc;
    }

    @Value("${elasticsearch.index}")
    private String INDEX;

    public ResultSet search(VehicleSearchConstraint constraint) throws IOException {

        // Make a request
        MultiSearchRequest multiSearchRequest = makeMultiSearchRequest(constraint);

        // Search
        MultiSearchResponse searchResponse = rhlc.msearch(multiSearchRequest, RequestOptions.DEFAULT);
        MultiSearchResponse.Item[] responses = searchResponse.getResponses();

        return ResultSet.builder()
                .maker(listCodes(responses[Field.MAKER.ordinal()], Field.MAKER.getCode()))
                .subModel(listCodes(responses[Field.SUB_MODEL.ordinal()], Field.SUB_MODEL.getCode()))
                .steering(listCodes(responses[Field.STEERING.ordinal()], Field.STEERING.getCode()))
                .fuel(listCodes(responses[Field.FUEL.ordinal()], Field.FUEL.getCode()))
                .transmission(listCodes(responses[Field.TRANSMISSION.ordinal()], Field.TRANSMISSION.getCode()))
                .condition(listCodes(responses[Field.CONDITION.ordinal()], Field.CONDITION.getCode()))
                .drivetrain(listCodes(responses[Field.DRIVETRAIN.ordinal()], Field.DRIVETRAIN.getCode()))
                .location(listCodes(responses[Field.LOCATION.ordinal()], Field.LOCATION.getCode()))
                .color(listCodes(responses[Field.COLOR.ordinal()], Field.COLOR.getCode()))
                .vehicleType(listCodes(responses[Field.VEHICLE_TYPE.ordinal()], Field.VEHICLE_TYPE.getCode()))
                .build();
    }

    /**
     * Multi Search 요청 파라미터를 생성한다.
     *
     * @return MultiSearchRequest
     */
    private MultiSearchRequest makeMultiSearchRequest(VehicleSearchConstraint constraint) {
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

        for (Field field : Field.values()) {
            SearchRequest searchRequest = getSearchRequest(INDEX, field.getCode(), field.getName(), constraint);
            multiSearchRequest.add(searchRequest);
        }

        return multiSearchRequest;
    }


    /**
     * 검색 결과 중 하나를 변환한다.
     *
     * @param response 검색 결과 중 코드 집합 하나
     * @return 결과 결과
     */
    private List<Result> listCodes(MultiSearchResponse.Item response, String aggName) {
        if (response == null) {
            return Collections.emptyList();
        }

        SearchResponse searchResponse = response.getResponse();
        if (searchResponse == null) {
            return Collections.emptyList();
        }

        Aggregations aggregations = searchResponse.getAggregations();
        List<Result> results = new ArrayList<>();
        Terms terms = aggregations.get(aggName);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            Result result = new Result(bucket.getKeyAsString(), "name_" + bucket.getKeyAsString(), bucket.getDocCount());
            results.add(result);
        }
        return results;
    }

    /**
     * Elasticsearch 집계 요청 정보를 생성한다.
     *
     * @param indexName 검색할 인덱스명
     * @param fieldName 집계할 필드명
     * @return 요청 정보
     */
    private SearchRequest getSearchRequest(String indexName, String aggName, String fieldName, VehicleSearchConstraint constraint) {
        final boolean desc = false;

        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("flag_del", "N"))
                .must(QueryBuilders.termQuery("status_cd", "C030"));

        String maker = constraint.getMaker();
        if (!aggName.equals(Field.MAKER.getCode()) && maker != null) {
            query = query.must(QueryBuilders.termQuery(Field.MAKER.getName(), maker));
        }

        String subModel = constraint.getSubModel();
        if (!aggName.equals(Field.SUB_MODEL.getCode()) && subModel != null) {
            query = query.must(QueryBuilders.termQuery(Field.SUB_MODEL.getName(), subModel));
        }

        String location = constraint.getLocation();
        if (!aggName.equals(Field.LOCATION.getCode()) && location != null) {
            query = query.must(QueryBuilders.termQuery(Field.LOCATION.getName(), location));
        }

        String condition = constraint.getCondition();
        if (!aggName.equals(Field.CONDITION.getCode()) && condition != null) {
            query = query.must(QueryBuilders.termQuery(Field.CONDITION.getName(), condition));
        }

        String vehicleType = constraint.getVehicleType();
        if (!aggName.equals(Field.VEHICLE_TYPE.getCode()) && vehicleType != null) {
            query = query.must(QueryBuilders.termQuery(Field.VEHICLE_TYPE.getName(), vehicleType));
        }

        String steering = constraint.getSteering();
        if (!aggName.equals(Field.STEERING.getCode()) && steering != null) {
            query = query.must(QueryBuilders.termQuery(Field.STEERING.getName(), steering));
        }

        String transmission = constraint.getTransmission();
        if (!aggName.equals(Field.TRANSMISSION.getCode()) && transmission != null) {
            query = query.must(QueryBuilders.termQuery(Field.TRANSMISSION.getName(), transmission));
        }

        String drivetrain = constraint.getDrivetrain();
        if (!aggName.equals(Field.DRIVETRAIN.getCode()) && drivetrain != null) {
            query = query.must(QueryBuilders.termQuery(Field.DRIVETRAIN.getName(), drivetrain));
        }

        String fuel = constraint.getFuel();
        if (!aggName.equals(Field.FUEL.getCode()) && fuel != null) {
            query = query.must(QueryBuilders.termQuery(Field.FUEL.getName(), fuel));
        }

        String color = constraint.getColor();
        if (!aggName.equals(Field.COLOR.getCode()) && color != null) {
            query = query.must(QueryBuilders.termQuery(Field.COLOR.getName(), color));
        }

        String passenger = constraint.getPassenger();
        if (!aggName.equals(Field.PASSENGER.getCode()) && passenger != null) {
            query = query.must(QueryBuilders.termQuery(Field.PASSENGER.getName(), passenger));
        }

        // .must(QueryStringQueryBuilder.parseInnerQueryBuilder(XContentBuilder.builder(XContent)))

        AggregationBuilder aggs = AggregationBuilders
                // .global("agg")
                .terms(aggName)
                .field(fieldName)
                .size(10_000)
                .order(BucketOrder.count(desc));

        SearchSourceBuilder searchSource = new SearchSourceBuilder()
                .size(0)
                .query(query)
                .aggregation(aggs);

        return new SearchRequest(indexName).source(searchSource);
    }
}
