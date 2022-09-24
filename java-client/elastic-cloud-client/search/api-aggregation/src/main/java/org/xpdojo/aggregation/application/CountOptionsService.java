package org.xpdojo.aggregation.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xpdojo.aggregation.dto.Option;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CountOptionsService {

    public List<Option> listOptions() {
        List<Option> options = Arrays.asList(
                new Option("1", "language", 499_499),
                new Option("2", "testing", 123_456_789),
                new Option("3", "container", 55_555_499)
        );
        for (Option option : options) {
            log.info("option >>> {}", option);
        }
        return options;
    }

}
