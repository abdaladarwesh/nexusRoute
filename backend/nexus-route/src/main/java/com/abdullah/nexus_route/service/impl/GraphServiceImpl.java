package com.abdullah.nexus_route.service.impl;

import com.abdullah.nexus_route.annotation.LogExecutionTime;
import com.abdullah.nexus_route.entity.CityEdge;
import com.abdullah.nexus_route.entity.CityNode;
import com.abdullah.nexus_route.exception.LocationNotFoundException;
import com.abdullah.nexus_route.exception.LocationType;
import com.abdullah.nexus_route.exception.NoShortPathWasFoundException;
import com.abdullah.nexus_route.model.Graph.Graph;
import com.abdullah.nexus_route.model.Graph.GraphEdge;
import com.abdullah.nexus_route.model.Graph.GraphNode;
import com.abdullah.nexus_route.model.Graph.ShortestRouteReturn;
import com.abdullah.nexus_route.repository.CityEdgeRepository;
import com.abdullah.nexus_route.repository.CityNodeRepository;
import com.abdullah.nexus_route.service.ArrayProcessService;
import com.abdullah.nexus_route.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GraphServiceImpl implements GraphService {
    private final CityEdgeRepository cityEdgeRepository;
    private final CityNodeRepository cityNodeRepository;
    private final ArrayProcessService arrayProcessService;
    private Graph<CityNode> graph = new Graph<>();

    @Override
    @LogExecutionTime
    public void addDataToGraph(List<CityNode> nodes, List<CityEdge> edges) throws Exception {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        arrayProcessService.processArrayAsync(nodes, numberOfCores, (chunk) -> {
            addNodes(chunk);
        }).join();

        arrayProcessService.processArrayAsync(edges, numberOfCores, (chunk) -> {
            addEdges(chunk);
        }).join();
    }

    @Override
    @Transactional(readOnly = true)
    @Async
    @LogExecutionTime
    public void initGraphFromDatabase() throws Exception {
        List<CityNode> nodes = cityNodeRepository.findAll();
        List<CityEdge> edges = cityEdgeRepository.findAll();
        addDataToGraph(nodes, edges);
    }

    @Override
    @Transactional
    @LogExecutionTime
    public void addGraphDataToDatabase(List<CityNode> cityNodes, List<CityEdge> cityEdges) {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        arrayProcessService.processArrayAsync(cityNodes, numberOfCores, (chunk) -> {
            cityNodeRepository.saveAll(chunk);
        }).join();

        arrayProcessService.processArrayAsync(cityEdges, numberOfCores, (chunk) -> {
            cityEdgeRepository.saveAll(chunk);
        }).join();
    }

    @Override
    public boolean isDatabaseEmpty() {
        return cityEdgeRepository.count() == 0 && cityNodeRepository.count() == 0;
    }

    @Override
    public ShortestRouteReturn<CityNode> findShortestPath(Long startId, Long destinationId) throws LocationNotFoundException, NoShortPathWasFoundException {
        Optional<CityNode> start = cityNodeRepository.findById(startId);
        Optional<CityNode> destination = cityNodeRepository.findById(destinationId);
        if (start.isEmpty()) {
            throw new LocationNotFoundException(startId, LocationType.START);
        }
        if (destination.isEmpty()) {
            throw new LocationNotFoundException(destinationId, LocationType.DESTINATION);
        }

        GraphNode<CityNode> startingNode = graph.addNodeIfNotExists(start.get());
        GraphNode<CityNode> destinationNode = graph.addNodeIfNotExists(destination.get());

        ShortestRouteReturn<CityNode> shortestRouteLog = graph.findShortestRouteLog(startingNode, destinationNode);


        return shortestRouteLog;

    }


    private void addNodes(List<CityNode> nodes) {
        for (CityNode node : nodes) {
            graph.addNodeIfNotExists(node);
        }
    }

    private void addEdges(List<CityEdge> edges) throws LocationNotFoundException{
        for (CityEdge edge : edges) {
            GraphNode<CityNode> startNode = graph.addNodeIfNotExists(edge.getStart());

            GraphNode<CityNode> destinationNode = graph.addNodeIfNotExists(edge.getDestination());

            graph.addEdge(startNode, destinationNode, edge.getWeight(), true);
        }
    }
}

