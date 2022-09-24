package org.xpdojo.search.application;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ResourceNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xpdojo.search.domain.Car;
import org.xpdojo.search.domain.CarSearchRepository;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CarService {

    private final CarSearchRepository carSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public CarService(
            CarSearchRepository carSearchRepository,
            ElasticsearchOperations elasticsearchOperations
    ) {
        this.carSearchRepository = carSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Car findById(String id) {
        return carSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
    }

    /**
     * JPA Repository를 사용한 검색
     */
    public Iterable<Car> findAll(int offset, int size) {
        PageRequest pageRequest = PageRequest.of(
                offset,
                size,
                Sort.by(
                        Sort.Order.desc("sort_point"),
                        Sort.Order.desc("@timestamp")
                ));
        return carSearchRepository.findAll(pageRequest);
    }

    /**
     * Native Query를 사용한 검색
     */
    public Iterable<Car> search(int offset, int size, String status) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("status_code.keyword", status))
                .must(QueryBuilders.termQuery("is_deleted.keyword", "N"));

        PageRequest pageRequest = PageRequest.of(
                offset,
                size,
                Sort.by(
                        Sort.Order.desc("sort_point"),
                        Sort.Order.desc("@timestamp")
                ));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .build();

        String query = nativeSearchQuery.getQuery().toString();
        log.debug("[search] query: {}", query);

        SearchHits<Car> hits = elasticsearchOperations.search(nativeSearchQuery, Car.class);
        SearchPage<Car> pages = SearchHitSupport.searchPageFor(hits, nativeSearchQuery.getPageable());
        return (Page) SearchHitSupport.unwrapSearchHits(pages);
    }

}
