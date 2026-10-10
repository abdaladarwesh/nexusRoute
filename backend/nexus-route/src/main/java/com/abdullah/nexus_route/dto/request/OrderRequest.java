package com.abdullah.nexus_route.dto;

import com.abdullah.nexus_route.entity.OrderPriority;
import com.abdullah.nexus_route.entity.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderRequest {


    @NotNull
    @NotBlank
    private Long pickupNodeId;

    @NotNull
    @NotBlank
    private Long destinationNodeId;

    @NotNull
    @NotBlank
    private OrderStatus status;

    @NotNull
    @NotBlank
    private OrderPriority priority;

}
