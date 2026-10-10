package com.abdullah.nexus_route.controller;

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
    public ResponseEntity<List<CityNode>> findShortestPath(@RequestParam Long startId, @RequestParam Long destinationId) throws LocationNotFoundException, NoShortPathWasFoundException {
        return ResponseEntity.ok(
                graphService.findShortestPath(startId, destinationId).getPath()
        );
    }


}
