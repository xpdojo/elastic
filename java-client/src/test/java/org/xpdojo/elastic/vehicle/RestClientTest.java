package org.xpdojo.elastic.vehicle;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.xpdojo.elastic.vehicle.model.Cluster;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles({"local", "production"})
@SpringBootTest
class RestClientTest {

    @Autowired
    private RestClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${elasticsearch.index}")
    private String INDEX;

    @Test
    void cluter_info() throws IOException {

        Request request = new Request(HttpMethod.GET.name(), "/");
        request.addParameter("pretty", "true");

        Response response = esClient.performRequest(request);
        try (final InputStream inputStream = response.getEntity().getContent()) {
            final Cluster cluster = objectMapper.readValue(inputStream, Cluster.class);
            log.info("cluster: {}", cluster);

            assertThat(cluster).isNotNull();
        }
    }

    @Test
    void cat() throws IOException {

        Request request = new Request(HttpMethod.GET.name(), "/_cat");
        request.addParameter("pretty", "true");

        final Response response = esClient.performRequest(request);
        final HttpEntity entity = response.getEntity();
        final String result = EntityUtils.toString(entity);
        log.info("result: {}", result);

        assertThat(result).startsWith("=^.^=");
    }

    @Test
    void list_index() throws IOException {

        Request request = new Request(HttpMethod.GET.name(), "/_cat/indices");
        request.addParameter("pretty", "true");

        final Response response = esClient.performRequest(request);
        final HttpEntity entity = response.getEntity();
        final String result = EntityUtils.toString(entity);
        log.info("result: {}", result);

        assertThat(result).isNotEmpty();
    }

    @Test
    void search_index() throws IOException {
        Request request = new Request(HttpMethod.POST.name(), INDEX + "/_search");
        // request.addParameter("pretty", "true");

        final Response searchReponse = esClient.performRequest(request);
        final String result = EntityUtils.toString(searchReponse.getEntity());
        log.info("result: {}", result);

        assertThat(result).isNotEmpty();
    }

}
