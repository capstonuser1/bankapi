package com.example.bankapi.dto;
import java.math.BigDecimal;
public record TransactionStatsDto(String type, BigDecimal amount) {
}