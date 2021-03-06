package org.xpdojo.elastic.vehicle.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Result {

    private String code;

    private String name;

    private long count;

    public Result(String code, String name, long count) {
        this.code = code;
        this.name = name;
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }
}
