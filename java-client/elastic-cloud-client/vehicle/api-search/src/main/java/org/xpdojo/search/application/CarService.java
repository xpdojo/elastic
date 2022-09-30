package org.xpdojo.search.application;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ResourceNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xpdojo.search.criteria.SearchCriteria;
import org.xpdojo.search.criteria.VehicleQuery;
import org.xpdojo.search.domain.Car;
import org.xpdojo.search.domain.CarSearchRepository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * 상품 카운팅
     *
     * @param searchCriteria
     * @return
     */
    public long count(SearchCriteria searchCriteria) {

        Map<String, String> criteria = searchCriteria.toCriteria();

        BoolQueryBuilder boolQueryBuilder = VehicleQuery.generate(criteria);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        String query = nativeSearchQuery.getQuery().toString();
        log.debug("[count] query: {}", query);

        return elasticsearchOperations.count(nativeSearchQuery, Car.class);
    }

    /**
     * Native Query를 사용한 상품 검색
     */
    public Iterable<Car> search(SearchCriteria searchCriteria) {

        Map<String, String> criteria = searchCriteria.toCriteria();

        BoolQueryBuilder boolQueryBuilder = VehicleQuery.generate(criteria);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder);

        // 페이징 처리
        PageRequest pageRequest = PageRequest.of(
                criteria.get("offset") == null ? 0 : Integer.parseInt(criteria.get("offset")),
                criteria.get("size") == null ? 30 : Integer.parseInt(criteria.get("size")),
                Sort.by(
                        Sort.Order.desc("sort_point"),
                        Sort.Order.desc("@timestamp")
                ));
        nativeSearchQueryBuilder.withPageable(pageRequest);

        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        Optional.ofNullable(nativeSearchQuery.getQuery())
                .ifPresent(query -> log.debug("[search] query: {}", query));

        SearchHits<Car> hits = elasticsearchOperations.search(nativeSearchQuery, Car.class);
        SearchPage<Car> pages = SearchHitSupport.searchPageFor(hits, nativeSearchQuery.getPageable());

        return pages.getContent().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}
