package com.example.bankapi.service;

import com.example.bankapi.dto.TransactionStatsDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.math.BigDecimal;

@Service
public class MessageConsumerService {
    private static final String TOPIC = "transaction-stats";
    private static final Logger log = LoggerFactory.getLogger(MessageConsumerService.class);
    private final com.example.bankapi.config.GlobalAmounts globalAmounts;

    public MessageConsumerService(com.example.bankapi.config.GlobalAmounts globalAmounts) {
        this.globalAmounts = globalAmounts;
    }

    @KafkaListener(topics = TOPIC)
    public void handle(ConsumerRecord<String, TransactionStatsDto> record) {
        TransactionStatsDto transactionStats = record.value();


        log.info("Received key={} type={} amount={} offset={}",
                record.key(),transactionStats.type() , transactionStats.amount(), record.offset());

        if ("TRANSFER_IN".equals(transactionStats.type())) {
            globalAmounts.addTransfer(transactionStats.amount());
        } else if ("UTILITY_PAYMENT".equals(transactionStats.type())) {
            globalAmounts.addUtilityPayment(transactionStats.amount());
        } else if ("DEPOSIT".equals(transactionStats.type())) {
            globalAmounts.addDeposit(transactionStats.amount());
        } else if ("WITHDRAWAL".equals(transactionStats.type())) {
            globalAmounts.addWithdrawal(transactionStats.amount());
        }

        log.info("Today's totalTransferAmount is amount={}", globalAmounts.getTotalTransferAmount());
        log.info("Today's totalUtilityPaymentAmount is amount={}", globalAmounts.getTotalUtilityPaymentAmount());
        log.info("Today's totalDipositsAmount is amount={}", globalAmounts.getTotalDepositsAmount());
        log.info("Today's totalCreditsAmount is amount={}", globalAmounts.getTotalWithdrawalAmount());

//        TransactionStatistic statistic = new TransactionStatistic(
//                transactionStats.type(),
//                transactionStats.amount()
//        );
//
//        collector.collect(statistic);
    }
}
