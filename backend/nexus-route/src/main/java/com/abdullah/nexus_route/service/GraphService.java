package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.entity.CityEdge;
import com.abdullah.nexus_route.entity.CityNode;
import com.abdullah.nexus_route.exception.LocationNotFoundException;
import com.abdullah.nexus_route.exception.NoShortPathWasFoundException;
import com.abdullah.nexus_route.model.Graph.ShortestRouteReturn;

import java.util.List;

public interface GraphService  {
    void addDataToGraph(List<CityNode> nodes, List<CityEdge> edges) throws Exception;
    void initGraphFromDatabase() throws Exception;
    void addGraphDataToDatabase(List<CityNode> cityNodes, List<CityEdge> cityEdges);
    boolean isDatabaseEmpty();
    ShortestRouteReturn<CityNode> findShortestPath(Long startId, Long destinationId) throws LocationNotFoundException, NoShortPathWasFoundException;



}
