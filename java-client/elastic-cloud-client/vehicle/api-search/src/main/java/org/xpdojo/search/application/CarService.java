package org.xpdojo.search.application;

import lombok.RequiredArgsConstructor;
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
import org.xpdojo.search.criteria.SearchVehicleCriteria;
import org.xpdojo.search.criteria.VehicleQuery;
import org.xpdojo.search.domain.Car;
import org.xpdojo.search.domain.CarSearchRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarSearchRepository carSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

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
     * @param searchVehicleCriteria
     * @return
     */
    public long count(SearchVehicleCriteria searchVehicleCriteria) {

        BoolQueryBuilder boolQueryBuilder = VehicleQuery.build(searchVehicleCriteria);
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
    public Iterable<Car> search(SearchVehicleCriteria searchVehicleCriteria) {

        BoolQueryBuilder boolQueryBuilder = VehicleQuery.build(searchVehicleCriteria);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder);

        // 페이징 처리
        PageRequest pageRequest = PageRequest.of(
                searchVehicleCriteria.getOffset() == null ? 0 : Integer.parseInt(searchVehicleCriteria.getOffset()),
                searchVehicleCriteria.getSize() == null ? 30 : Integer.parseInt(searchVehicleCriteria.getSize()),
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
