package org.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.statistics.model.StatisticsSummary;
import org.statistics.service.StatisticsService;

/**
 * REST API for consuming statistics requests
 *
 * @author zaguliyev
 * @since 1.0.0
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public StatisticsSummary getStatistics(){
        return statisticsService.getStatistics();
    }
}
