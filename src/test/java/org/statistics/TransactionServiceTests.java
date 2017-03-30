package org.statistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.statistics.exception.ValidationException;
import org.statistics.model.Transaction;
import org.statistics.service.StatisticsServiceImpl;
import org.statistics.service.TransactionService;
import org.statistics.service.TransactionServiceImpl;

import static org.mockito.Mockito.*;

/**
 * @author zaur.guliyev
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;


    @Mock
    private StatisticsServiceImpl statisticsServiceMock;

    @InjectMocks
    private TransactionServiceImpl transactionServiceMock;


    @Test(expected = ValidationException.class)
    public void whenEmptyRequestBody_exceptionThrown(){
        transactionService.addTransaction(null);
    }

    @Test(expected = ValidationException.class)
    public void whenMissingTimestampField_exceptionThrown(){
        transactionService.addTransaction(new Transaction(12.5, null));
    }

    @Test(expected = ValidationException.class)
    public void whenMissingAmountField_exceptionThrown(){
        transactionService.addTransaction(new Transaction(null, System.currentTimeMillis()));
    }

    @Test
    public void whenValidTransaction_flowSucceeds(){
        doNothing().when(statisticsServiceMock).computeStatistics(any(Transaction.class));
        transactionServiceMock.addTransaction(new Transaction(12.5, System.currentTimeMillis()));

        verify(statisticsServiceMock, times(1)).computeStatistics(any(Transaction.class));
    }
}
