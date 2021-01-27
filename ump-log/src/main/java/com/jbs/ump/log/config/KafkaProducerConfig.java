package com.jbs.ump.log.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @created by wjf
 * @date 2019/12/18 18:34
 * @description:
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Value("${kafka.brokers}")
    private String brokers;

    public Map producerConfigs() {
        Map<String,Object> props = new LinkedHashMap<>();
        //bootstrap.servers"
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        //acks
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        //retries
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        //batch.size
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //linger.ms
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        //key.serializer
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //value.serializer
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    public ProducerFactory producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
