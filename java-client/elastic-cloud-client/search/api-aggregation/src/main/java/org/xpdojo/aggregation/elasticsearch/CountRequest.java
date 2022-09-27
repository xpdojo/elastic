package org.xpdojo.aggregation.elasticsearch;

import lombok.Getter;

@Getter
public class CountRequest {

    private String status;
    private String maker;

    public CountRequest(String status, String maker) {
        this.status = status;
        this.maker = maker;
    }
}
