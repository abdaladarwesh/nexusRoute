package com.abdullah.nexus_route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Object metadata;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
