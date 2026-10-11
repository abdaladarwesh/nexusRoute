package com.abdullah.nexus_route.dto.request;

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


    @NotNull(message = "pickupNodeId is required!")
    @NotBlank(message = "pickupNodeId is required!")
    private Long pickupNodeId;

    @NotNull(message = "destination is required!")
    @NotBlank(message = "destination is required!")
    private Long destinationNodeId;

    @NotNull(message = "status is required!")
    @NotBlank(message = "status is required!")
    private OrderStatus status;

    @NotNull(message = "priority is required!")
    @NotBlank(message = "priority is required!")
    private OrderPriority priority;

}
