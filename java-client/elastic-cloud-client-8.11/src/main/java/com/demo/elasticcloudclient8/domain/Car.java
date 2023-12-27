package com.demo.elasticcloudclient8.domain;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

// JPA에서 @Entity와 같은 역할
@Document(indexName = "#{@environment.getProperty('elasticsearch.index.product.car')}")
@Getter
public class Car {

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
