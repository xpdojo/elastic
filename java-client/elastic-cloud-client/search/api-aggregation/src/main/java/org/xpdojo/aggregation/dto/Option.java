package org.xpdojo.aggregation.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Option {

    private String code;
    private long count;

    public Option(String code, long count) {
        this.code = code;
        this.count = count;
    }
}
