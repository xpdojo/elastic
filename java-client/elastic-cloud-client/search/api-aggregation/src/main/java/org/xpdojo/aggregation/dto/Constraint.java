package org.xpdojo.aggregation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Constraint {
    List<Option> makers;
    List<Option> models;
    List<Option> status;
}
