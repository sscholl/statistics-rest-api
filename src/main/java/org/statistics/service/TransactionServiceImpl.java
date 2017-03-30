package org.statistics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.statistics.common.APIError;
import org.statistics.exception.ValidationException;
import org.statistics.model.Transaction;

/**
 * Holds business logic for processing transactions
 *
 * @author zaur.guliyev
 * @since 1.0.0
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private StatisticsService statisticsService;

    @Override
    public void addTransaction(Transaction transaction) {
        if(transaction == null) throw new ValidationException(APIError.VALIDATION_EMPTY_REQUEST_BODY);
        if(transaction.getTimestamp() == null) throw new ValidationException(APIError.VALIDATION_MISSING_TIMESTAMP);
        if(transaction.getAmount() == null) throw new ValidationException(APIError.VALIDATION_MISSING_AMOUNT);

        logger.info("Received new transaction => {}", transaction);
        statisticsService.computeStatistics(transaction);
    }
}
