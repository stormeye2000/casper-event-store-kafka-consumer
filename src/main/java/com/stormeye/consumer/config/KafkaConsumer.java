package com.stormeye.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
public abstract class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class.getName());

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.client-id}")
    private String clientId;


    final List<Disposable> disposables = new ArrayList<>();

    public abstract Flux<?> flux();

    public void run() {
        flux().blockLast();
        close();
    }

    public void close() {
        for (Disposable disposable : disposables)
            disposable.dispose();
    }

    public ReceiverOptions<Integer, String> receiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //Group id is client_id from producer i think
        props.put(ConsumerConfig.GROUP_ID_CONFIG, clientId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return ReceiverOptions.create(props);
    }

    public ReceiverOptions<Integer, String> receiverOptions(Collection<String> topics) {
        return receiverOptions()
                .addAssignListener(p -> log.info("Group {} partitions assigned {}", clientId, p))
                .addRevokeListener(p -> log.info("Group {} partitions revoked {}", clientId, p))
                .subscription(topics);
    }


}
