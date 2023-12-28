package com.demo.elasticcloudclient8.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
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
     * Spring Data Elasticsearch 5.x부터 {@link RestHighLevelClient}가 deprecated 되고
     * {@link ElasticsearchClient}를 사용한다. {@code AbstractElasticsearchConfiguration} 추상 클래스를 상속받으면
     * 반환 타입이 {@link RestHighLevelClient}이기 때문에 상위 클래스
     * {@link ElasticsearchConfigurationSupport}를 상속한다.
     *
     * <pre>
     * // ElasticsearchConfigurationSupport에는 elasticsearchClient()가 없다.
     * // AbstractElasticsearchConfiguration extends ElasticsearchConfigurationSupport
     * <code>@Bean</code>
     * public abstract RestHighLevelClient elasticsearchClient();
     * </pre>
     *
     * @see <a href="https://docs.spring.io/spring-data/elasticsearch/docs/5.1.6/reference/html/#elasticsearch-migration-guide-4.4-5.0.new-clients">Upgrading from 4.4.x to 5.0.x</a>
     */
    // @Bean
    public RestHighLevelClient deprecated() {
        final Header[] headers = new Header[]{new BasicHeader("Authorization", "ApiKey " + apiKey)};
        final RestClientBuilder builder =
                RestClient
                        .builder("cloud-id")
                        .setDefaultHeaders(headers);
        return new RestHighLevelClient(builder);
    }

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
