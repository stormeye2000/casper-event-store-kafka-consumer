package com.stormeye.consumer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.stormeye.consumer.service.KafkaSource;

@Component
public class StartUp implements ApplicationRunner {

    final KafkaSource service;

    public StartUp(final KafkaSource service) {
        this.service = service;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        service.run();
    }
}
