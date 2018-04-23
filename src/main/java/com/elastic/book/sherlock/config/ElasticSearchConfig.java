package com.elastic.book.sherlock.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author liguolin
 * @create 2018-03-22 11:05
 **/
@Configuration
public class ElasticSearchConfig {


    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private int port;

    @Value("${es.elasticsearch.cluster.name}")
    private String clusterName;


    /**
     *
     *
     * 当ES服务器监听使用内网服务器IP而访问使用外网IP时，不要使用client.transport.sniff为true，
     * 在自动发现时会使用内网IP进行通信，导致无法连接到ES服务器，而直接使用addTransportAddress方法进行指定ES服务器。
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public Client createElasticClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
//                .put("client.transport.sniff", true).build();

        Client client = new PreBuiltTransportClient(settings).addTransportAddress(
                new TransportAddress(InetAddress.getByName(host), port));

        return client;
    }
}
