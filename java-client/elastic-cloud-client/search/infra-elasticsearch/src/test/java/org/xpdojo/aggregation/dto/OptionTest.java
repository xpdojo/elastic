package org.xpdojo.aggregation.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionTest {

    @Test
    void test_option() {
        Option actual = new Option("1", "k8s", 3);
        Integer expected = 3;

        assertEquals(expected, actual.getCount());
    }

}
