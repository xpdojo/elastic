package org.xpdojo.elastic.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cluster {

    private String name;
    private String clusterName;
    private String clusterUuid;
    private Version version;
    private String tagline;

}
