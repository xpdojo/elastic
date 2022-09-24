package org.xpdojo.search.domain;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Document(indexName = "test-vehicle-car-2022.09.24")
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
