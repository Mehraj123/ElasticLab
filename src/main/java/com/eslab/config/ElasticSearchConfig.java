package com.eslab.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${elastic.host}")
    private String host;
    @Value("${elastic.port}")
    private int port;
    private static final String HTTP_SCHEME = "http";
    private Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        logger.info("Connecting to Elastic Search Engine ...");
        RestHighLevelClient highLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, HTTP_SCHEME)));
        checkESConnection(highLevelClient);
        return highLevelClient;
    }

    private void checkESConnection(RestHighLevelClient highLevelClient) {
        try {
            boolean isConnected = highLevelClient.ping(RequestOptions.DEFAULT);
            if (isConnected) {
                logger.info("Connected to Elastic search engine..!");
            }
        } catch (Exception e) {
            logger.error("Failed to Connected to Elastic search engine.", e);
        }
    }
}
