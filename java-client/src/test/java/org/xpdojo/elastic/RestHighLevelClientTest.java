package org.xpdojo.elastic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.xpdojo.elastic.vehicle.SearchService;
import org.xpdojo.elastic.vehicle.model.CodeSet;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles({"local", "production"})
@SpringBootTest
class RestHighLevelClientTest {

    @Autowired
    private SearchService searchService;

    @Test
    void multi_search() throws IOException {
        final LocalTime start = LocalTime.now();

        CodeSet codeSet = searchService.search();

        final LocalTime end = LocalTime.now();
        log.info("duration: {}", ChronoUnit.MILLIS.between(start, end));

        assertThat(codeSet).isNotNull();
    }

}
