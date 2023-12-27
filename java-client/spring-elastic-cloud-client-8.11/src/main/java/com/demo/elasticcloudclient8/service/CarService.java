package com.demo.elasticcloudclient8.service;

import com.demo.elasticcloudclient8.domain.Car;
import com.demo.elasticcloudclient8.domain.CarSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarSearchRepository carSearchRepository;

    /**
     * Elasticsearch Repository 사용한 검색
     */
    public List<Car> findAll(int offset, int size) {
        PageRequest pageRequest = PageRequest.of(
                offset,
                size,
                Sort.by(
                        Sort.Order.desc("sort_point"),
                        Sort.Order.desc("@timestamp")
                ));
        return carSearchRepository
                .findAll(pageRequest)
                .toList();
    }

    /**
     * Keyword 조건으로 ElasticsearchOperations 사용한 검색
     */
    public List<Car> findByPrice(
            final int priceFrom,
            final int priceTo,
            final int offset,
            final int size
    ) {
        return carSearchRepository
                .findByPrice(priceFrom, priceTo, offset, size);
    }

    /**
     * Keyword 조건으로 ElasticsearchOperations 사용한 검색
     */
    public List<Car> findByKeyword(
            final String keyword,
            final int offset,
            final int size
    ) {
        return carSearchRepository
                .findByKeyword(keyword, offset, size);
    }

}
