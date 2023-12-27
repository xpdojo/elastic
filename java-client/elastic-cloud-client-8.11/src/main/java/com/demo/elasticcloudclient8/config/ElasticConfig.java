package com.demo.elasticcloudclient8.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

@Configuration
// @EnableElasticsearchRepositories(basePackages = "com.demo.elasticcloudclient8")
public class ElasticConfig extends ElasticsearchConfigurationSupport {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.api-key}")
    private String apiKey;

    /**
     * low-level client
     */
    @Bean
    public RestClient restClient() {
        return RestClient
                .builder(new HttpHost(host, port, "https"))
                /*
                    Authorization를 low-level client에서 설정하거나 Transport 옵션으로 추가할 수 있음.
                 */
                // .setDefaultHeaders(
                //         new Header[]{
                //                 new BasicHeader("Authorization", "ApiKey " + apiKey)
                //         })
                .build();
    }

    /**
     * high-level client
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        /*
            Authorization를 low-level client에서 설정하거나 Transport 옵션으로 추가할 수 있음.
         */
        RestClientOptions options =
                new RestClientOptions(
                        RequestOptions.DEFAULT.toBuilder()
                                .addHeader("Authorization", "ApiKey " + apiKey)
                                .build()
                );

        // high-level에서 요청 전송(transport) 시 사용할 Rest Client 설정
        RestClientTransport transport =
                new RestClientTransport(
                        restClient(),
                        new JacksonJsonpMapper(),
                        options);

        // High-level client 생성
        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(
            ElasticsearchClient elasticsearchClient,
            ElasticsearchConverter elasticsearchConverter
    ){
        return new ElasticsearchTemplate(elasticsearchClient, elasticsearchConverter);
    }

}
