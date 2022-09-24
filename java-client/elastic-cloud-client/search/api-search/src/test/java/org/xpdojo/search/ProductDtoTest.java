package org.xpdojo.search;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDtoTest {

    @Test
    void test_product() {
        ProductDto actual = new ProductDto(1, "k8s", BigInteger.valueOf(111L));
        String expected = "k8s";
        assertEquals(expected, actual.getName());
    }
}
