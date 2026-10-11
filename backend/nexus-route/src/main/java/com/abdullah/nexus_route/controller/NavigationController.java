package com.abdullah.nexus_route.controller;

import com.abdullah.nexus_route.dto.response.ApiResponse;
import com.abdullah.nexus_route.entity.CityNode;
import com.abdullah.nexus_route.exception.LocationNotFoundException;
import com.abdullah.nexus_route.exception.NoShortPathWasFoundException;
import com.abdullah.nexus_route.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/path")
public class NavigationController {
    private final GraphService graphService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CityNode>>> findShortestPath(@RequestParam Long startId, @RequestParam Long destinationId) throws LocationNotFoundException, NoShortPathWasFoundException {
        return ResponseEntity.ok(
                ApiResponse.<List<CityNode>>builder()
                        .data(graphService.findShortestPath(startId, destinationId).getPath())
                        .message("Shortest path retrieved successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }
}
