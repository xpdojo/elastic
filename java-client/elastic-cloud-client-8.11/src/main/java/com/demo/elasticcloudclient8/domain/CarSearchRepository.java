package com.demo.elasticcloudclient8.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarSearchRepository extends ElasticsearchRepository<Car, String> {
}
