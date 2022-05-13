package com.stormeye.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverOptions;

public abstract class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class.getName());

    final ServiceProperties properties;
    final List<Disposable> disposables = new ArrayList<>();

    public KafkaConsumer(final ServiceProperties properties) {
        this.properties = properties;
    }

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
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafka().getServer() + ":" + properties.getKafka().getPort());
        //Group id is client_id from producer i think
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getKafka().getClient());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.getKafka().getClient());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return ReceiverOptions.create(props);
    }

    public ReceiverOptions<Integer, String> receiverOptions(Collection<String> topics) {
        return receiverOptions()
                .addAssignListener(p -> log.info("Group {} partitions assigned {}", properties.getKafka().getClient(), p))
                .addRevokeListener(p -> log.info("Group {} partitions revoked {}", properties.getKafka().getClient(), p))
                .subscription(topics);
    }


}
