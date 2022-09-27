package org.xpdojo.aggregation.application;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xpdojo.aggregation.dto.Option;
import org.xpdojo.aggregation.dto.SearchCriteria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Slf4j
@Service
public class CountOptionsService {

    @Value("${elasticsearch.index.vehicle}")
    private String INDEX;

    private final RestHighLevelClient client;

    public CountOptionsService(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * Native Query를 사용한 검색
     */
    public Map<String, List<Option>> aggregateVehicleOptions(SearchCriteria searchCriteria) {

        MultiSearchRequest multiSearchRequest = multiSearchRequest(searchCriteria);

        try {
            MultiSearchResponse msearch = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
            return extractOptionAggregations(msearch);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }

        return Collections.emptyMap();
    }

    /**
     * 집계할 데이터
     *
     * @param searchCriteria 검색 조건
     * @return 집계 요청
     */
    protected MultiSearchRequest multiSearchRequest(SearchCriteria searchCriteria) {

        Map<String, String> terms = searchCriteria.toTerms();
        Map<String, String> criteria = searchCriteria.toCriteria();

        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

        for (Map.Entry<String, String> term : terms.entrySet()) {
            TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                    .terms(term.getKey())
                    .field(term.getValue());

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            if (criteria == null || criteria.isEmpty()) {
                boolQueryBuilder.must(matchAllQuery());
            } else {
                criteria.forEach((key, value) -> boolQueryBuilder.must(matchQuery(key, value)));
            }

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                    .query(boolQueryBuilder)
                    .size(0)
                    .from(0)
                    .aggregation(termsAggregationBuilder);

            SearchRequest searchRequest = new SearchRequest()
                    .indices(INDEX)
                    .source(searchSourceBuilder);

            log.debug("querytest {}", searchRequest.source().toString());

            multiSearchRequest.add(searchRequest);
        }

        return multiSearchRequest;
    }

    /**
     * 집계 응답에서 content 추출
     *
     * @param msearch 응답
     * @return 집계 결과
     */
    public Map<String, List<Option>> extractOptionAggregations(MultiSearchResponse msearch) {

        MultiSearchResponse.Item[] msearchResponses = msearch.getResponses();
        Map<String, List<Option>> constraint = new HashMap<>();

        for (MultiSearchResponse.Item item : msearchResponses) {
            if (item == null) {
                continue;
            }

            SearchResponse searchResponse = item.getResponse();
            if (searchResponse == null) {
                continue;
            }

            Aggregations aggregations = searchResponse.getAggregations();
            for (Aggregation aggregation : aggregations) {
                if (aggregation instanceof ParsedStringTerms) {
                    ParsedStringTerms parsedStringTerms = (ParsedStringTerms) aggregation;
                    constraint.put(parsedStringTerms.getName(), getOptions(parsedStringTerms));
                    continue;
                }

                if (aggregation instanceof ParsedLongTerms) {
                    ParsedLongTerms parsedLongTerms = (ParsedLongTerms) aggregation;
                    constraint.put(parsedLongTerms.getName(), getOptions(parsedLongTerms));
                }
            }
        }

        log.debug("constraint {}", constraint);

        return constraint;
    }

    /**
     * option 집계 결과 추출
     *
     * @param term 집계 결과
     * @return option 집계 결과
     */
    private List<Option> getOptions(ParsedTerms term) {
        List<Option> options = new ArrayList<>();

        for (Terms.Bucket bucket : term.getBuckets()) {
            Option option = new Option(bucket.getKeyAsString(), bucket.getDocCount());
            options.add(option);
        }

        return options;
    }

}
