package org.xpdojo.search.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class CarDto {

    private String id;
    private String makerCode;
    private String transmissionCode;
    private String statusCode;
    private int sortPoint;
    private Date listingDtm;

}
