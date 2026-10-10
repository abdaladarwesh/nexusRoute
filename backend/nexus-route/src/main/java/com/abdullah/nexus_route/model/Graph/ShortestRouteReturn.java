package com.abdullah.nexus_route.model.Graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortestRouteReturn<T> {
    Double distance;
    List<T> path;
}
