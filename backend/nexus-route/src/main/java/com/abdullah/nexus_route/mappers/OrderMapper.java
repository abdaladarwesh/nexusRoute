package com.abdullah.nexus_route.mappers;

import com.abdullah.nexus_route.dto.request.OrderRequest;
import com.abdullah.nexus_route.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequest request);
    List<Order> toEntity(List<OrderRequest> request);
}
