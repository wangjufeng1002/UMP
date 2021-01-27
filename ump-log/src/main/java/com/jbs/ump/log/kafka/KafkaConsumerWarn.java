package com.jbs.ump.log.kafka;

import com.jbs.ump.log.sevice.LogWarningService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @created by wjf
 * @date 2019/12/25 13:52
 * @description:
 */
@Component
public class KafkaConsumerWarn {

    @Resource
    private LogWarningService logWarningService;

    @KafkaListener(topics = {"${zcm-log-warn.topic:hermes-log}",},containerFactory = "kafkaListenerContainerFactoryWarn")
    public void consumer(ConsumerRecord<?, ?> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get().toString());
            logWarningService.analysizeLog(kafkaMessage.get().toString());
        }
    }
}
