package org.statistics.service;

import org.statistics.model.Transaction;

/**
 * @author zaur.guliyev
 * @since 1.0.0
 */
public interface TransactionService {
    void addTransaction(Transaction transaction);
}
