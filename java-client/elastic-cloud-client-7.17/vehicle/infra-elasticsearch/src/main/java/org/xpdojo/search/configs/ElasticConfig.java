package org.xpdojo.search.configs;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

// @EnableElasticsearchAuditing
@EnableElasticsearchRepositories(basePackages = "org.xpdojo.search")
@Configuration
public class ElasticConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.cloud-id}")
    private String cloudId;

    @Value("${elasticsearch.api-key}")
    private String apiKey;

    /**
     * TODO: RestHighLevelClient(HLRC)가 deprecated 되었고, ElasticsearchClient(EC)를 사용하라고 권장한다.
     *
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/migrate-hlrc.html">Migrating from the High Level Rest Client</a>
     */
    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        // Create the low-level client
        RestClient httpClient =
                RestClient
                        .builder(cloudId)
                        .setDefaultHeaders(
                                new Header[]{
                                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                                }
                        ).build();

        // Create the HLRC
        return new RestHighLevelClientBuilder(httpClient)
                .setApiCompatibilityMode(true)
                .build();
    }

}
