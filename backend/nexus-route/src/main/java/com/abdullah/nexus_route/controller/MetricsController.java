package com.abdullah.nexus_route.controller;

import com.abdullah.nexus_route.dto.response.ApiResponse;
import com.abdullah.nexus_route.dto.response.ErrorRateResponse;
import com.abdullah.nexus_route.dto.response.PercentileResponse;
import com.abdullah.nexus_route.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/metrics")
public class MetricsController {
    private final MetricsService metricsService;

    @GetMapping("/errorRate")
    public ResponseEntity<ApiResponse<ErrorRateResponse>> getErrorRate() {
        Double errorRate = metricsService.getOverallErrorRate();

        return ResponseEntity.ok(
                ApiResponse.<ErrorRateResponse>builder()
                        .data(
                                ErrorRateResponse.builder()
                                        .errorRate(errorRate)
                                        .status(metricsService.calculateStatus(errorRate))
                                        .build()
                        )
                        .message("Error rate retrieved successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }

    @GetMapping("/errorRateBetweenStartAndEnd")
    public ResponseEntity<ApiResponse<ErrorRateResponse>> getErrorRateBetweenStartAndEnd(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        Double errorRate = metricsService.getOverallErrorRateBetweenStartAndEnd(start, end);

        return ResponseEntity.ok(
                ApiResponse.<ErrorRateResponse>builder()
                        .data(
                                ErrorRateResponse.builder()
                                        .errorRate(errorRate)
                                        .status(metricsService.calculateStatus(errorRate))
                                        .build()
                        )
                        .message("Error rate retrieved successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }

    @GetMapping("/percentile/{percentile}")
    public ResponseEntity<ApiResponse<PercentileResponse>> getPercentile(@PathVariable Double percentile) {
        Long percentileValue = metricsService.getPercentile(percentile);

        return ResponseEntity.ok(
                ApiResponse.<PercentileResponse>builder()
                        .data(
                                PercentileResponse.builder()
                                        .executionTimeMs(percentileValue)
                                        .status(metricsService.calculatePercentileStatus(percentileValue))
                                        .build()
                        )
                        .message("Percentile retrieved successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }
}
