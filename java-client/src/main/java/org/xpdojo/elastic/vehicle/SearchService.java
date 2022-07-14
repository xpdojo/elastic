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
import org.xpdojo.elastic.vehicle.model.Code;
import org.xpdojo.elastic.vehicle.model.CodeSet;

import java.io.IOException;
import java.util.ArrayList;
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

    private final String MAKER_CODE = "PC0008";
    private final String MAKER_FIELD = "make_cd";
    private final String SUB_MODEL_CODE = "PC0294";
    private final String SUB_MODEL_FIELD = "model_cd";
    // private final String MODEL_CODE = "PC0294";
    // private final String MODEL_FIELD = "dtl_model_cd";

    private final String LOCATION_CODE = "PC0003";
    private final String LOCATION_FIELD = "location_cd";
    private final String CONDITION_CODE = "PC0006";
    private final String CONDITION_FIELD = "type_cd";
    private final String VEHICLE_TYPE_CODE = "PC0007";
    private final String VEHICLE_TYPE_FIELD = "vehicle_type";
    private final String STEERING_CODE = "PC0009";
    private final String STEERING_FIELD = "steering_cd";
    private final String TRANSMISSION_CODE = "PC0010";
    private final String TRANSMISSION_FIELD = "transmission_cd";
    private final String DRIVETRAIN_CODE = "PC0011";
    private final String DRIVETRAIN_FIELD = "drivetrain_cd";
    private final String FUEL_CODE = "PC0012";
    private final String FUEL_FIELD = "fuel_type_cd";
    private final String COLOR_CODE = "PC0015";
    private final String COLOR_FIELD = "exterior_color_cd";

    // private final String OPTION_CODE = "PC0021";
    // private final String OPTION_FIELD = "option_cd";
    private final String PASSENGER_CODE = "passenger";
    private final String PASSENGER_FIELD = "passenger";

    public CodeSet search() throws IOException {

        Request request = new Request(HttpMethod.GET.name(), "/");
        request.addParameter("pretty", "true");

        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        SearchRequest searchRequestMaker = getSearchRequest(INDEX, MAKER_CODE, MAKER_FIELD);
        multiSearchRequest.add(searchRequestMaker);
        SearchRequest searchRequestSubModel = getSearchRequest(INDEX, SUB_MODEL_CODE, SUB_MODEL_FIELD);
        multiSearchRequest.add(searchRequestSubModel);
        SearchRequest searchRequestSteering = getSearchRequest(INDEX, STEERING_CODE, STEERING_FIELD);
        multiSearchRequest.add(searchRequestSteering);
        SearchRequest searchRequestFuel = getSearchRequest(INDEX, FUEL_CODE, FUEL_FIELD);
        multiSearchRequest.add(searchRequestFuel);
        SearchRequest searchRequestTransmission = getSearchRequest(INDEX, TRANSMISSION_CODE, TRANSMISSION_FIELD);
        multiSearchRequest.add(searchRequestTransmission);
        SearchRequest searchRequestCondition = getSearchRequest(INDEX, CONDITION_CODE, CONDITION_FIELD);
        multiSearchRequest.add(searchRequestCondition);
        SearchRequest searchRequestDrivetrain = getSearchRequest(INDEX, DRIVETRAIN_CODE, DRIVETRAIN_FIELD);
        multiSearchRequest.add(searchRequestDrivetrain);
        SearchRequest searchRequestLocation = getSearchRequest(INDEX, LOCATION_CODE, LOCATION_FIELD);
        multiSearchRequest.add(searchRequestLocation);
        SearchRequest searchRequestColor = getSearchRequest(INDEX, COLOR_CODE, COLOR_FIELD);
        multiSearchRequest.add(searchRequestColor);
        SearchRequest searchRequestVehicleType = getSearchRequest(INDEX, VEHICLE_TYPE_CODE, VEHICLE_TYPE_FIELD);
        multiSearchRequest.add(searchRequestVehicleType);
        SearchRequest searchRequestPassenger = getSearchRequest(INDEX, PASSENGER_CODE, PASSENGER_FIELD);
        multiSearchRequest.add(searchRequestPassenger);

        MultiSearchResponse searchResponse = rhlc.msearch(multiSearchRequest, RequestOptions.DEFAULT);
        MultiSearchResponse.Item[] responses = searchResponse.getResponses();

        CodeSet codeSet = CodeSet.builder()
                .maker(listCodes(responses[0], MAKER_CODE))
                .subModel(listCodes(responses[1], SUB_MODEL_CODE))
                .steering(listCodes(responses[2], STEERING_CODE))
                .fuel(listCodes(responses[3], FUEL_CODE))
                .transmission(listCodes(responses[4], TRANSMISSION_CODE))
                .condition(listCodes(responses[5], CONDITION_CODE))
                .drivetrain(listCodes(responses[6], DRIVETRAIN_CODE))
                .location(listCodes(responses[7], LOCATION_CODE))
                .color(listCodes(responses[8], COLOR_CODE))
                .vehicleType(listCodes(responses[9], VEHICLE_TYPE_CODE))
                .build();

        log.info("code set : {}", codeSet);

        return codeSet;
    }


    /**
     * 응답 파싱
     *
     * @param response 검색 결과 중 코드 집합 하나
     * @return 결과 코드 목록
     */
    private List<Code> listCodes(MultiSearchResponse.Item response, String metaCode) {
        SearchResponse searchResponse = response.getResponse();
        Aggregations aggregations = searchResponse.getAggregations();
        List<Code> codes = new ArrayList<>();
        ParsedStringTerms terms = aggregations.get(metaCode);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            Code code = new Code(bucket.getKeyAsString(), "name_" + bucket.getKeyAsString(), bucket.getDocCount());
            codes.add(code);
        }
        return codes;
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

        if (!aggName.equals(COLOR_CODE)) {
            query = query.must(QueryBuilders.termQuery("exterior_color_cd", "C180"));
        }

        if (!aggName.equals(TRANSMISSION_CODE)) {
            query = query.must(QueryBuilders.termQuery("transmission_cd", "C010"));
        }

        // .must(QueryStringQueryBuilder.parseInnerQueryBuilder(XContentBuilder.builder(XContent)))

        SearchSourceBuilder searchSource = new SearchSourceBuilder()
                .size(0)
                .query(query)
                .aggregation(aggs);

        return new SearchRequest(indexName).source(searchSource);
    }
}
