package com.stormeye.consumer.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.stormeye.consumer.config.KafkaConsumer;
import com.stormeye.consumer.config.ServiceProperties;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;

@Service
public class KafkaSource extends KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaReceiver.class.getName());

    private final Scheduler scheduler;
    private final ServiceProperties properties;

    public KafkaSource(@Qualifier("ServiceProperties") final ServiceProperties properties) {
        super(properties);
        this.scheduler = Schedulers.newSingle("event-scheduler", true);
        this.properties = properties;
    }

    public Flux<?> flux() {

        return KafkaReceiver.create(receiverOptions(properties.getKafka().getTopics()).commitInterval(Duration.ZERO))
                .receive()
                .publishOn(scheduler)
                .concatMap(m -> storeInDB(m.value())
                        .thenEmpty(m.receiverOffset().commit()))
                .retry()
                .doOnCancel(this::close);
    }


    public Mono<Void> storeInDB(String person) {
        log.info("Successfully processed person with id {} from Kafka", person);
        return Mono.empty();
    }


    public void close() {
        super.close();
        scheduler.dispose();
    }

}