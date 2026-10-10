package com.abdullah.nexus_route.service.impl;

import com.abdullah.nexus_route.annotation.LogExecutionTime;
import com.abdullah.nexus_route.entity.CityNode;
import com.abdullah.nexus_route.entity.Order;
import com.abdullah.nexus_route.exception.LocationNotFoundException;
import com.abdullah.nexus_route.exception.LocationType;
import com.abdullah.nexus_route.model.Graph.ShortestRouteReturn;
import com.abdullah.nexus_route.repository.CityNodeRepository;
import com.abdullah.nexus_route.repository.OrderRepository;
import com.abdullah.nexus_route.service.ArrayProcessService;
import com.abdullah.nexus_route.service.GraphService;
import com.abdullah.nexus_route.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ArrayProcessService arrayProcessService;
    private final OrderRepository orderRepository;
    private final CityNodeRepository cityNodeRepository;
    private final GraphService graphService;
    private final ObjectMapper objectMapper;

    @Override
    @LogExecutionTime
    public void dispatchOrders(List<Order> orders) {

        for (Order order : orders) {
            Optional<CityNode> pickupNode = cityNodeRepository.findById(order.getPickupNodeId());
            Optional<CityNode> destinationNode = cityNodeRepository.findById(order.getDestinationNodeId());
            if (pickupNode.isEmpty()) {
                throw new LocationNotFoundException(order.getPickupNodeId(), LocationType.START);
            }
            if (destinationNode.isEmpty()) {
                throw new LocationNotFoundException(order.getDestinationNodeId(), LocationType.DESTINATION);
            }
            ShortestRouteReturn<CityNode> shortestPath = graphService.findShortestPath(order.getPickupNodeId(), order.getDestinationNodeId());
            order.setTotalDistance(shortestPath.getDistance());
            order.setRoutePathJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortestPath.getPath()));
            order.setDispatchedAt(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            savedOrder.setTrackingNumber("ORD-" + savedOrder.getId());
            orderRepository.save(savedOrder);
        }
    }

    @Override
    @Transactional
    @LogExecutionTime
    public void dispatchOrdersAsync(List<Order> orders) {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        arrayProcessService.processArrayAsync(orders, numberOfCores, (chunk) -> {
            for (Order order : chunk) {
                Optional<CityNode> pickupNode = cityNodeRepository.findById(order.getPickupNodeId());
                Optional<CityNode> destinationNode = cityNodeRepository.findById(order.getDestinationNodeId());
                if (pickupNode.isEmpty()) {
                    throw new LocationNotFoundException(order.getPickupNodeId(), LocationType.START);
                }
                if (destinationNode.isEmpty()) {
                    throw new LocationNotFoundException(order.getDestinationNodeId(), LocationType.DESTINATION);
                }
                ShortestRouteReturn<CityNode> shortestPath = graphService.findShortestPath(order.getPickupNodeId(), order.getDestinationNodeId());
                order.setTotalDistance(shortestPath.getDistance());
                order.setRoutePathJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortestPath.getPath()));
                order.setDispatchedAt(LocalDateTime.now());
                Order savedOrder = orderRepository.save(order);
                savedOrder.setTrackingNumber("ORD-" + savedOrder.getId());
                orderRepository.save(savedOrder);
            }
        });
    }
}
