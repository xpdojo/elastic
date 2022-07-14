package org.xpdojo.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableElasticsearchAuditing
// @EnableElasticsearchRepositories
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    /**
     * <code>RestHighLevelClient</code>가 7.15.0에서 deprecated
     *
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/migrate-hlrc.html">Migrating from the High Level Rest Clientedit</a>
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html">Java High Level REST Clientedit</a>
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder(
                new HttpHost(host, port, "http")
        ).build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClientBuilder(restClient()).build();
    }
}
