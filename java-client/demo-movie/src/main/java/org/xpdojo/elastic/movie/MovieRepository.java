package org.xpdojo.elastic.movie;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {

    Iterable<Movie> findByTitle(String title);

}
