package org.xpdojo.search.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CarSearchRepository extends ElasticsearchRepository<Car, String>, CustomCarSearchRepository {

    List<Car> findByStatusCode(String statusCode);

}
