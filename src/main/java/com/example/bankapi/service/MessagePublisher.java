package com.example.bankapi.service;

import com.example.bankapi.dto.TransactionStatsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@Service
public class MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(MessagePublisher.class);
    private static final String TOPIC = "transaction-stats";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MessagePublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    public void publish_test(TransactionStatsDto transaction) {
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(TOPIC, transaction.type(), transaction);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish transaction-stat  for  {}",
                        transaction, ex);
            } else {
                var metadata = result.getRecordMetadata();
                log.info("Sent {} -> partition {}, offset {}",
                        transaction,
                        metadata.partition(),
                        metadata.offset());
            }
        });
    }
}
