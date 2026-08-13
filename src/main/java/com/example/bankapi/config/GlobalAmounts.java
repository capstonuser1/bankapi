package com.example.bankapi.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Spring bean holding four global amount totals.
 * Thread-safe updates via AtomicReference.
 */
@Component
public class GlobalAmounts {

    private final AtomicReference<BigDecimal> totalTransfer = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<BigDecimal> totalUtilityPayment = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<BigDecimal> totalDeposits = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<BigDecimal> totalWithdrawal = new AtomicReference<>(BigDecimal.ZERO);

    public void addTransfer(BigDecimal amount) {
        if (amount == null) return;
        totalTransfer.updateAndGet(v -> v.add(amount));
    }

    public void addUtilityPayment(BigDecimal amount) {
        if (amount == null) return;
        totalUtilityPayment.updateAndGet(v -> v.add(amount));
    }

    public void addDeposit(BigDecimal amount) {
        if (amount == null) return;
        totalDeposits.updateAndGet(v -> v.add(amount));
    }

    public void addWithdrawal(BigDecimal amount) {
        if (amount == null) return;
        totalWithdrawal.updateAndGet(v -> v.add(amount));
    }

    public BigDecimal getTotalTransferAmount() {
        return totalTransfer.get();
    }

    public BigDecimal getTotalUtilityPaymentAmount() {
        return totalUtilityPayment.get();
    }

    public BigDecimal getTotalDepositsAmount() {
        return totalDeposits.get();
    }

    public BigDecimal getTotalWithdrawalAmount() {
        return totalWithdrawal.get();
    }

    public void resetAll() {
        totalTransfer.set(BigDecimal.ZERO);
        totalUtilityPayment.set(BigDecimal.ZERO);
        totalDeposits.set(BigDecimal.ZERO);
        totalWithdrawal.set(BigDecimal.ZERO);
    }
}
