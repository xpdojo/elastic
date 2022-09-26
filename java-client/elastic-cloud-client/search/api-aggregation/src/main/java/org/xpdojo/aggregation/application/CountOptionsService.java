package org.xpdojo.aggregation.application;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.xpdojo.aggregation.dto.Constraint;
import org.xpdojo.aggregation.dto.Option;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Slf4j
@Service
public class CountOptionsService {

    private final String INDEX = "test-vehicle-car-2022.09.24";

    private final RestHighLevelClient client;

    public CountOptionsService(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * Native Query를 사용한 검색
     */
    public Constraint aggregations(String status) {

        TermsAggregationBuilder makers = AggregationBuilders
                .terms("makers")
                .field("maker_code.keyword");

        BoolQueryBuilder filter = new BoolQueryBuilder()
                .filter(matchAllQuery());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(filter)
                .size(0)
                .from(0)
                .aggregation(makers);

        SearchRequest aggregateMaker = new SearchRequest()
                .indices(INDEX)
                .source(searchSourceBuilder);

        log.debug("querytest {}", aggregateMaker.source().toString());

        MultiSearchRequest searchRequest = new MultiSearchRequest()
                .add(aggregateMaker);

        try {
            MultiSearchResponse msearch = client.msearch(searchRequest, RequestOptions.DEFAULT);
            return process(msearch);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }

        return null;
    }

    private Constraint process(MultiSearchResponse msearch) {

        MultiSearchResponse.Item[] responses = msearch.getResponses();
        Constraint constraint = new Constraint();
        List<Option> options = new ArrayList<>();

        for (MultiSearchResponse.Item response : responses) {
            SearchResponse searchResponse = response.getResponse();
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms makers = aggregations.get("makers");
            makers.getBuckets().forEach(bucket -> {
                options.add(new Option(bucket.getKeyAsString(), null, bucket.getDocCount()));
                log.info("maker: {}, count: {}", bucket.getKeyAsString(), bucket.getDocCount());
            });
        }

        constraint.setMakers(options);

        return constraint;
    }

}
