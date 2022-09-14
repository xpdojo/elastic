package org.xpdojo.elastic.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Version {

    private String number;
    private String buildFlavor;
    private String buildType;
    private String buildHash;
    private String buildDate;
    private String buildSnapshot;
    private String luceneVersion;
    private String minimumWireCompatibilityVersion;
    private String minimumIndexCompatibilityVersion;

}
