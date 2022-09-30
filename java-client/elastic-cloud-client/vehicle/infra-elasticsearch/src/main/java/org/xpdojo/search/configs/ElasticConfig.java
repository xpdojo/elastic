package org.xpdojo.search.configs;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
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
     * RestHighLevelClient가 deprecated 되었지만
     * Spring Data Elasticsearch가 아직 RestHighLevelClient를 사용한다.
     */
    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final Header[] headers = new Header[]{new BasicHeader("Authorization", "ApiKey " + apiKey)};
        RestClientBuilder builder = RestClient.builder(cloudId).setDefaultHeaders(headers);
        return new RestHighLevelClient(builder);
    }

}
