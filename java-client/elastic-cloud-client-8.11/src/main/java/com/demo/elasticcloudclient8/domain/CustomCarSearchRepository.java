package com.demo.elasticcloudclient8.domain;

import java.util.List;

public interface CustomCarSearchRepository {

    List<Car> findByKeyword(String keyword, int offset, int size);

}
