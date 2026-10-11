package com.abdullah.nexus_route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorRateResponse {
    Double errorRate;
    ErrorRateStatus status;
}
