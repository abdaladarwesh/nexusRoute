package com.abdullah.nexus_route.service.impl;

import com.abdullah.nexus_route.dto.response.ErrorRateStatus;
import com.abdullah.nexus_route.dto.response.PercentileStatus;
import com.abdullah.nexus_route.repository.ExecutionLogRepository;
import com.abdullah.nexus_route.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {
    private final ExecutionLogRepository executionLogRepository;

    @Override
    public Double getOverallErrorRate() {
        return executionLogRepository.calculateOverallErrorRate();
    }

    @Override
    public Double getOverallErrorRateBetweenStartAndEnd(LocalDateTime start, LocalDateTime end) {
        return executionLogRepository.calculateOverallErrorRateBetweenStartAndEnd(start, end);
    }

    @Override
    public Long getPercentile(double percentile) {
        List<Long> executionTimes = executionLogRepository.findAllExecutionTimeMsByIsErrorIsNull();
        if (executionTimes == null || executionTimes.isEmpty()) {
            return null;
        }

        int index = (int) Math.ceil(percentile * executionTimes.size());

        index = Math.min(Math.max(index - 1, 0), executionTimes.size() - 1);

        return executionTimes.get(index);
    }

    @Override
    public ErrorRateStatus calculateStatus(Double errorRate) {
        if (errorRate == null) {
            return ErrorRateStatus.UNKNOWN;
        }

        if (errorRate > 0.05) {
            return ErrorRateStatus.CRITICAL;
        } else if (errorRate > 0.03) {
            return ErrorRateStatus.WARNING;
        } else if (errorRate > 0.01) {
            return ErrorRateStatus.ELEVATED;
        } else {
            return ErrorRateStatus.NORMAL;
        }
    }

    @Override
    public PercentileStatus calculatePercentileStatus(Long executionTimeMs) {
        if (executionTimeMs == null) {
            return PercentileStatus.UNKNOWN;
        }

        if (executionTimeMs > 1000) {
            return PercentileStatus.CRITICAL;
        } else if (executionTimeMs > 500) {
            return PercentileStatus.WARNING;
        } else if (executionTimeMs > 200) {
            return PercentileStatus.ELEVATED;
        } else {
            return PercentileStatus.NORMAL;
        }
    }
}
