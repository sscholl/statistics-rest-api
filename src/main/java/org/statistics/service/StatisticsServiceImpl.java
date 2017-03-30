package org.statistics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.statistics.model.Statistics;
import org.statistics.model.StatisticsSummary;
import org.statistics.model.Transaction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds business logic for computing statistics summary
 *
 * @author zaur.guliyev
 * @since 1.0.0
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private static final int SECONDS_STAT = 60;
    private static final Map<Integer, Statistics> statisticsForLastMin = new ConcurrentHashMap<>(SECONDS_STAT);

    /**
     * Re-calculates statistics for last 60 seconds based on new received transaction data.
     * Transaction data summary (sum, max, min, count) is kept inside map, for each second of last minute new summary entry is created
     * or existing one is updated with latest statistics for that second, if previously inserted entry
     * is outdated then it will be overwritten by new one(s) (if no new transaction entry is received
     * for that second then it'll be simply ignored during statistics retrieval).
     *
     * Application holds constant memory storage (at max 60 entries) about statistics of last minute,
     * which means memory complexity is O(1)
     *
     * @param transaction new transaction data
     */
    @Override
    public void computeStatistics(Transaction transaction) {
        logger.info("Computing statistics based on new received transaction => {}", transaction);

        if ((System.currentTimeMillis() - transaction.getTimestamp()) / 1000 < SECONDS_STAT) {
            int second = LocalDateTime.ofInstant(Instant.ofEpochMilli(transaction.getTimestamp()), ZoneId.systemDefault()).getSecond();
            statisticsForLastMin.compute(second, (k, v) -> {
                if (v == null || (System.currentTimeMillis() - v.getTimestamp()) / 1000 >= SECONDS_STAT) {
                    v = new Statistics();
                    v.setTimestamp(transaction.getTimestamp());
                    v.setSum(transaction.getAmount());
                    v.setMax(transaction.getAmount());
                    v.setMin(transaction.getAmount());
                    v.setCount(1l);
                    return v;
                }

                v.setCount(v.getCount() + 1);
                v.setSum(v.getSum() + transaction.getAmount());
                if (Double.compare(transaction.getAmount(), v.getMax()) > 0) v.setMax(transaction.getAmount());
                if (Double.compare(transaction.getAmount(), v.getMin()) < 0) v.setMin(transaction.getAmount());
                return v;
            });
        }
    }

    /**
     * Calculates and returns combined statistics summary based on statistics map (statisticsForLastMin).
     * During calculation outdated statistics are ignored.
     *
     * Calculation is made in constant time by only combining already calculated statistics, which means
     * method runs with O(1) complexity
     *
     * @return combined statistics summary
     */
    @Override
    public StatisticsSummary getStatistics() {
        StatisticsSummary summary = statisticsForLastMin.values().stream()
                .filter(s -> (System.currentTimeMillis() - s.getTimestamp()) / 1000 < SECONDS_STAT)
                .map(StatisticsSummary::new)
                .reduce(new StatisticsSummary(), (s1, s2) -> {
                    s1.setSum(s1.getSum() + s2.getSum());
                    s1.setCount(s1.getCount() + s2.getCount());
                    s1.setMax(Double.compare(s1.getMax(), s2.getMax()) > 0 ? s1.getMax() : s2.getMax());
                    s1.setMin(Double.compare(s1.getMin(), s2.getMin()) < 0 ? s1.getMin() : s2.getMin());
                    return s1;
                });

        summary.setMin(Double.compare(summary.getMin(), Double.MAX_VALUE) == 0 ? 0.0 : summary.getMin());
        summary.setMax(Double.compare(summary.getMax(), Double.MIN_VALUE) == 0 ? 0.0 : summary.getMax());
        summary.setAvg(summary.getCount() > 0l ? summary.getSum() / summary.getCount() : 0.0);

        logger.info("Statistics summary for last minute => {}", summary);
        return summary;
    }
}
