package org.xpdojo.elastic.vehicle;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
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

    public ResultSet search() throws IOException {

        Request request = new Request(HttpMethod.GET.name(), "/");
        request.addParameter("pretty", "true");

        MultiSearchRequest multiSearchRequest = makeMultiSearchRequest();

        MultiSearchResponse searchResponse = rhlc.msearch(multiSearchRequest, RequestOptions.DEFAULT);
        MultiSearchResponse.Item[] responses = searchResponse.getResponses();

        ResultSet resultSet = ResultSet.builder()
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

        log.info("code set : {}", resultSet);

        return resultSet;
    }

    /**
     * Multi Search 요청 파라미터를 생성한다.
     *
     * @return MultiSearchRequest
     */
    private MultiSearchRequest makeMultiSearchRequest() {
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

        for (Field field : Field.values()) {
            SearchRequest searchRequest = getSearchRequest(INDEX, field.getCode(), field.getName());
            multiSearchRequest.add(searchRequest);
        }

        return multiSearchRequest;
    }


    /**
     * 응답 파싱
     *
     * @param response 검색 결과 중 코드 집합 하나
     * @return 결과 코드 목록
     */
    private List<Result> listCodes(MultiSearchResponse.Item response, String metaCode) {
        if (response == null) {
            return Collections.emptyList();
        }

        SearchResponse searchResponse = response.getResponse();
        Aggregations aggregations = searchResponse.getAggregations();
        List<Result> results = new ArrayList<>();
        ParsedStringTerms terms = aggregations.get(metaCode);
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
    private SearchRequest getSearchRequest(String indexName, String aggName, String fieldName) {
        boolean DESC = false;

        AggregationBuilder aggs = AggregationBuilders
                // .global("agg")
                .terms(aggName)
                .field(fieldName)
                .order(BucketOrder.count(DESC));

        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("flag_del", "N"))
                .must(QueryBuilders.termQuery("status_cd", "C030"));

        if (!aggName.equals(Field.COLOR.getCode())) {
            query = query.must(QueryBuilders.termQuery(Field.COLOR.getName(), "C180"));
        }

        if (!aggName.equals(Field.TRANSMISSION.getCode())) {
            query = query.must(QueryBuilders.termQuery(Field.TRANSMISSION.getName(), "C010"));
        }

        // .must(QueryStringQueryBuilder.parseInnerQueryBuilder(XContentBuilder.builder(XContent)))

        SearchSourceBuilder searchSource = new SearchSourceBuilder()
                .size(0)
                .query(query)
                .aggregation(aggs);

        return new SearchRequest(indexName).source(searchSource);
    }
}
