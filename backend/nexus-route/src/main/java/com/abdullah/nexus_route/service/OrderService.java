package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.entity.Order;

import java.util.List;

public interface OrderService {
    void dispatchOrders(List<Order> orders);
    void dispatchOrdersAsync(List<Order> orders);
}
