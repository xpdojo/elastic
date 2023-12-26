package org.xpdojo.search.domain;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 애플리케이션에서 RDB로 다시 조회하기 때문에 식별자(id)와 같이 확인해야 할 정보만 담는다.
 */
// @Document(indexName = "#{@vehicleCarIndexName}")
@Document(indexName = "#{@environment.getProperty('elasticsearch.index.product.car')}")
@Entity
@Getter
public class Car {

    protected Car() {
    }

    @Id
    private String id;

    @Field(name = "maker_code", type = FieldType.Keyword)
    private String makerCode;

    @Field(name = "transmission_code", type = FieldType.Keyword)
    private String transmissionCode;

    @Field(name = "status_code", type = FieldType.Keyword)
    private String statusCode;

    @Field(name = "sort_point", type = FieldType.Integer)
    private int sortPoint;

    @Field(name = "@timestamp", type = FieldType.Date, format = DateFormat.date_time)
    private Date listingDtm;

}
