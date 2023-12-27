package com.demo.elasticcloudclient8.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * {@code @JsonIgnoreProperties(ignoreUnknown=true)}가 없으면
 * "com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field"
 * 에러가 발생한다.
 */
@Getter
// JPA에서 @Entity와 같은 역할
@Document(
        indexName = "#{@environment.getProperty('elasticsearch.index.product.car')}",
        createIndex = false
)
// Field가 추가되어도 무시하고 넘어감
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
