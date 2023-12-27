package com.demo.elasticcloudclient8.service;

import com.demo.elasticcloudclient8.domain.Car;
import com.demo.elasticcloudclient8.domain.CarSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarSearchRepository carSearchRepository;

    /**
     * Elasticsearch Repository 사용한 검색
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

}
