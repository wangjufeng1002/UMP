package com.jbs.ump.log.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @created by wjf
 * @date 2019/12/18 18:36
 * @description:
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${kafka.brokers}")
    private String brokers;

    @Value("${kafka.groupid.warn:zcm_log_warn}")
    private String groupidWarn;
    @Value("${kafka.groupid.ump:zm_log_ump}")
    private String groupidUmp;

    public Map<String,Object> consumerConfigs(){
        Map<String,Object> props = new LinkedHashMap<>();
        //bootstrap.servers
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);
        //enable.auto.commit
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        //auto.commit.interval.ms
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"100");
        //session.timeout.ms
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"15000");
        //注意此处反序列化，别搞错了，跟生产消息不一样
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
      /*  //groupid
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupid);*/
        //auto.offset.reset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        return props;
    }

    public ConsumerFactory<String,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactoryWarn(){
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerWarnFactory1());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactoryUmp(){
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerUmpFactory2());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }
    public ConsumerFactory<String, String> consumerWarnFactory1() {
        Map<String, Object> properties = consumerConfigs();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupidWarn);
        return new DefaultKafkaConsumerFactory<String, String>(properties);
    }

    public ConsumerFactory<String, String> consumerUmpFactory2() {
        Map<String, Object> properties = consumerConfigs();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupidUmp);
        return new DefaultKafkaConsumerFactory<String, String>(properties);
    }

}
