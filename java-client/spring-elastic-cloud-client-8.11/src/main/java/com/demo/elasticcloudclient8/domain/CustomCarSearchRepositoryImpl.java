package com.demo.elasticcloudclient8.domain;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

/**
 * JPA를 활용하기 힘든 복잡한 QueryDSL을 작성할 때 {@link ElasticsearchOperations}를 사용한다.
 * 동적 쿼리를 어떻게 작성해야 유지 보수 하기 좋을지 고민해야 한다.
 *
 * @see <a href="https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/template.html#elasticsearch.operations.criteriaquery">Criteria Query</a>
 * @see <a href="https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/template.html#elasticsearch.operations.nativequery">Native Query</a>
 */
@Slf4j
@RequiredArgsConstructor
public class CustomCarSearchRepositoryImpl implements CustomCarSearchRepository {

    // low-level client를 사용할 경우 @Document를 사용하지 못 하고 @JsonProperty로 직접 매핑해야 한다.
    // private final ElasticsearchClient elasticsearchClient;

    // spring framework에서 제공하는 ElasticsearchOperations를 사용할 경우 @Document를 사용할 수 있다.
    // ElasticsearchOperations Bean은 ElasticsearchClient를 사용한다.
    // ref: https://docs.spring.io/spring-data/elasticsearch/docs/5.1.2/reference/html/#elasticsearch-migration-guide-4.4-5.0.new-clients
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<Car> findByPrice(
            final int priceFrom,
            final int priceTo,
            final int offset,
            final int size
    ) {
        CriteriaQuery query =
                new CriteriaQuery(
                        new Criteria("product_price")
                                .greaterThanEqual(priceFrom)
                                .lessThanEqual(priceTo))
                        .setPageable(PageRequest.of(offset, size));

        log.debug("query: {}", query.getCriteria());

        SearchHits<Car> hits = elasticsearchOperations.search(query, Car.class);
        List<SearchHit<Car>> searchHits = hits.getSearchHits();
        return searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
    }

    @Override
    public List<Car> findByKeyword(
            final String keyword,
            final int offset,
            final int size
    ) {
        final NativeQuery query = NativeQuery.builder()
                .withQuery(builder -> builder
                        .bool(b -> b
                                .must(m -> m
                                        .multiMatch(multiMatch -> multiMatch
                                                .query(keyword)
                                                .fields("product_name",
                                                        "maker_name",
                                                        "model_name")
                                                .operator(Operator.And)
                                        )
                                )
                        )
                )
                .withPageable(PageRequest.of(offset, size))
                .build();
        // final NativeSearchQuery query = new NativeSearchQueryBuilder()
        //         .withQuery(
        //                 boolQuery()
        //                         .must(
        //                                 multiMatchQuery(keyword)
        //                                         .field("product_name")
        //                                         .field("maker_name")
        //                                         .field("model_name")
        //                                         .operator(Operator.AND)
        //                         )
        //         )
        //         .withPageable(PageRequest.of(offset, size))
        //         .build();

        // https://stackoverflow.com/a/76688038
        // https://docs.spring.io/spring-data/elasticsearch/docs/5.1.6/reference/html/#elasticsearch-migration-guide-4.4-5.0.breaking-changes-packages
        // spring-data-elasticsearch 4.4 -> 5.0로 변경되면서 NativeSearchQuery 대신 NativeQuery를 사용한다.
        // java.lang.IllegalArgumentException:
        // unhandled Query implementation org.springframework.data.elasticsearch.core.query.NativeSearchQuery
        // -> org.springframework.data.elasticsearch.client.elc.NativeQuery
        SearchHits<Car> hits = elasticsearchOperations.search(query, Car.class);
        List<SearchHit<Car>> searchHits = hits.getSearchHits();
        return searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
    }

}
