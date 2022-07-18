package com.stormeye.consumer.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.stormeye.consumer.config.KafkaConsumer;

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
    private final TopicsService topicsService;
    private final Environment environment;

    public KafkaSource(final TopicsService topicsService, final Environment environment) {
        super(environment);
        this.topicsService = topicsService;
        this.environment = environment;
        this.scheduler = Schedulers.newSingle("event-scheduler", true);
    }

    public Flux<?> flux() {

        return KafkaReceiver.create(receiverOptions(topicsService.getTopics()).commitInterval(Duration.ZERO))
                .receive()
                .publishOn(scheduler)
                .concatMap(m -> processEvent(m.value())
                        .thenEmpty(m.receiverOffset().commit()))
                .retry()
                .doOnCancel(this::close);
    }


    public Mono<Void> processEvent(String event) {
        log.info("Successfully processed topic [{}]: event {}", topicsService.getTopic(event), event);
        return Mono.empty();
    }


    public void close() {
        super.close();
        scheduler.dispose();
    }

}