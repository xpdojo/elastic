package com.demo.elasticcloudclient8.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @see <a href="https://docs.spring.io/spring-data/elasticsearch/reference/repositories/query-keywords-reference.html">Repository query keywords</a>
 */
@Repository
public interface CarSearchRepository
        extends ElasticsearchRepository<Car, String>, CustomCarSearchRepository {
}
