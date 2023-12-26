package org.xpdojo.search.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarSearchRepository extends ElasticsearchRepository<Car, String>, CustomCarSearchRepository {

    List<Car> findByStatusCode(String statusCode);

}
