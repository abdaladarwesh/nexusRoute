package com.abdullah.nexus_route.model.Graph;


import com.abdullah.nexus_route.entity.CityNode;
import com.abdullah.nexus_route.exception.LocationNotFoundException;
import com.abdullah.nexus_route.exception.NoShortPathWasFoundException;
import com.abdullah.nexus_route.model.CustomMinHeap.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Graph<T> {


    ConcurrentMap<GraphNode<T>, List<GraphEdge<T>>> adjList = new ConcurrentHashMap<>();

    public void addNodeToGraphIfNotExists(GraphNode<T> node) {
        adjList.putIfAbsent(node, Collections.synchronizedList(new ArrayList<>()));
    }

    public GraphNode<T> addNodeIfNotExists(T data) {
        GraphNode<T> node = new GraphNode<>(data);
        addNodeToGraphIfNotExists(node);
        return node;
    }

    public void addEdge(GraphNode<T> start, GraphNode<T> destination, Double weight) throws LocationNotFoundException {
        addEdge(start, destination, weight, true);
    }

    public void addEdge(GraphNode<T> start, GraphNode<T> destination, Double weight, boolean bidirectional) throws LocationNotFoundException {
        if (start == null) {
            throw new LocationNotFoundException(start.getData() + "is null");
        }
        if (destination == null) {
            throw new LocationNotFoundException(destination.getData() + "is null");
        }
        addNodeToGraphIfNotExists(start);
        addNodeToGraphIfNotExists(destination);
        GraphEdge<T> edge = new GraphEdge<T>(destination, weight);
        adjList.get(start).add(edge);
        if (bidirectional) {
            GraphEdge<T> reverseEdge = new GraphEdge<T>(start, weight);
            adjList.get(destination).add(reverseEdge);
        }
    }


    public ShortestRouteReturn<T> findShortestRouteLog(GraphNode<T> start, GraphNode<T> destination) throws NoShortPathWasFoundException {

        Map<GraphNode<T>, Double> distances = new HashMap<>();
        Map<GraphNode<T>, GraphNode<T>> previousNodes = new HashMap<>();

        // initialize fields
        MinHeap<GraphNode<T>> heap = new MinHeap<GraphNode<T>>();
        heap.add(new HeapNode<GraphNode<T>>(start, 0.0));

        for (GraphNode<T> node : adjList.keySet()) {
            if (node.equals(start)) {
                distances.put(node, 0.0);
            } else {
                distances.put(node, Double.MAX_VALUE);
            }
            previousNodes.put(node, null);
        }


        while (!heap.isEmpty()) {

            // getting the smallest unvisited node in distance
            HeapNode<GraphNode<T>> smallestParent = heap.poll();
            GraphNode<T> currentNode = smallestParent.getNode();
            Double currentDistance = smallestParent.getValue();


            if (currentNode == null) {
                break;
            }

            if (currentNode.equals(destination)) {


                // preparing the result
                List<GraphNode<T>> path = new ArrayList<>();
                GraphNode<T> startingNode = destination;
                while (startingNode != null) {
                    path.add(startingNode);
                    startingNode = previousNodes.get(startingNode);
                }
                Collections.reverse(path);

                return ShortestRouteReturn.<T>builder()
                        .distance(distances.get(destination))
                        .path(path.stream().map(GraphNode::getData).toList())
                        .build();


            }

            // relaxation for each edge in the node
            List<GraphEdge<T>> currentNodeEdges = adjList.get(currentNode);
            for (GraphEdge<T> edge : currentNodeEdges) {
                GraphNode<T> edgeDestination = edge.getDestination();
                Double edgeDestinationDistance = distances.get(edgeDestination);
                if (edge.getWeight() + currentDistance < edgeDestinationDistance) {
                    distances.put(edgeDestination, edge.getWeight() + currentDistance);
                    previousNodes.put(edgeDestination, currentNode);
                    heap.add(new HeapNode<>(edgeDestination, edge.getWeight() + currentDistance));
                }

            }


        }
        throw new NoShortPathWasFoundException("No Short path was found");
    }


//    public Map<List<GraphNode<T>>, Integer> findShortestRouteLinear(GraphNode<T> start, GraphNode<T> destination) throws Exception {
//
//        List<GraphNode<T>> unvisitedNodes = new ArrayList<>();
//        Map<GraphNode<T>, Integer> distances = new HashMap<>();
//        Map<GraphNode<T>, GraphNode<T>> previousNodes = new HashMap<>();
//
//        // initialize fields
//        for (GraphNode<T> node : adjList.keySet()) {
//            if (node.equals(start)) {
//                distances.put(node, 0);
//            } else {
//                distances.put(node, Integer.MAX_VALUE);
//            }
//            unvisitedNodes.add(node);
//            previousNodes.put(node, null);
//        }
//
//
//        while (!unvisitedNodes.isEmpty()) {
//
//            // getting the smallest unvisited node in distance
//            GraphNode<T> currentNode = null;
//            Integer currentDistance = Integer.MAX_VALUE;
//
//            for (GraphNode<T> node : unvisitedNodes) {
//                Integer currentNodeDistance = distances.get(node);
//                if (currentNodeDistance < currentDistance) {
//                    currentDistance = currentNodeDistance;
//                    currentNode = node;
//                }
//            }
//
//            if (currentNode == null) {
//                break;
//            }
//
//            if (currentNode.equals(destination)) {
//
//                // preparing the result
//
//                List<GraphNode<T>> path = new ArrayList<>();
//                GraphNode<T> startingNode = destination;
//                while (startingNode != null) {
//                    path.add(startingNode);
//                    startingNode = previousNodes.get(startingNode);
//                }
//
//                Collections.reverse(path);
//
//                Map<List<GraphNode<T>>, Integer> result = new HashMap<>();
//                result.put(path, currentDistance);
//                return result;
//            }
//
//            // relaxation for each edge in the node
//            List<GraphEdge<T>> currentNodeEdges = adjList.get(currentNode);
//            for (GraphEdge<T> edge : currentNodeEdges) {
//                GraphNode<T> edgeDestination = edge.getDestination();
//                Integer edgeDestinationDistance = distances.get(edgeDestination);
//                if (edge.getWeight() + currentDistance < edgeDestinationDistance) {
//                    // if the distance is shorter
//                    distances.put(edgeDestination, edge.getWeight() + currentDistance);
//                    previousNodes.put(edgeDestination, currentNode);
//                }
//
//            }
//
//            unvisitedNodes.remove(currentNode);
//
//        }
//
//        throw new Exception("No Short path was found");
//    }
//

}