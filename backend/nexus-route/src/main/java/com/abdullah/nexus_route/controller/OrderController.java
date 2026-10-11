package com.abdullah.nexus_route.controller;

import com.abdullah.nexus_route.dto.request.OrderRequest;
import com.abdullah.nexus_route.dto.response.ApiResponse;
import com.abdullah.nexus_route.dto.response.OrderDispatchResponse;
import com.abdullah.nexus_route.mappers.OrderMapper;
import com.abdullah.nexus_route.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDispatchResponse>> dispatchOrders(@RequestBody List<OrderRequest> request) {
        orderService.dispatchOrdersAsync(orderMapper.toEntity(request));

        OrderDispatchResponse dispatchResponse = OrderDispatchResponse.builder()
                .message("Order Dispatch intiated")
                .statusCode(200)
                .build();

        return ResponseEntity.ok(
                ApiResponse.<OrderDispatchResponse>builder()
                        .data(dispatchResponse)
                        .message("Order dispatch initiated successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }
}
