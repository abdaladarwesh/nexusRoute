package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.dto.response.ErrorRateStatus;
import com.abdullah.nexus_route.dto.response.PercentileStatus;

import java.time.LocalDateTime;

public interface MetricsService {
    Double getOverallErrorRate();
    Double getOverallErrorRateBetweenStartAndEnd(LocalDateTime start ,LocalDateTime end);
    Long getPercentile(double percentile);

    ErrorRateStatus calculateStatus(Double errorRate);

    PercentileStatus calculatePercentileStatus(Long executionTimeMs);
}
