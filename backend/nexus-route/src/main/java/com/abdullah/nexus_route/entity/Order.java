package com.abdullah.nexus_route.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private Long pickupNodeId;

    @Column(nullable = false)
    private Long destinationNodeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderPriority priority;

    private Double totalDistance;

    @Column(columnDefinition = "CLOB")
    private String routePathJson;

    private LocalDateTime createdAt;
    private LocalDateTime dispatchedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.QUEUED;
    }

}