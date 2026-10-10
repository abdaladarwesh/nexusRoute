package Graph;

import CustomMinHeap.*;

import java.util.*;

public class Graph<T> {


    Map<Node<T>, List<Edge<T>>> adjList = new HashMap<>();

    public void addNodeToGraphIfNotExists(Node<T> node) {
        adjList.putIfAbsent(node, new ArrayList<>());
    }

    public Node<T> createNode(T data) {
        Node<T> node = new Node<>(data);
        addNodeToGraphIfNotExists(node);
        return node;
    }

    public void addEdge(Node<T> start, Node<T> destination, int weight) throws Exception {
        if (start == null || destination == null) throw new Exception("Node Are Null");
        addNodeToGraphIfNotExists(start);
        addNodeToGraphIfNotExists(destination);
        Edge<T> edge = new Edge<T>(destination, weight);
        adjList.get(start).add(edge);
    }




    public Map<List<Node<T>>, Integer> findShortestRouteLog(Node<T> start, Node<T> destination) throws Exception {

        Map<Node<T>, Integer> distances = new HashMap<>();
        Map<Node<T>, Node<T>> previousNodes = new HashMap<>();

        // initialize fields
        MinHeap<Node<T>> heap = new MinHeap<Node<T>>();
        heap.add(new HeapNode<Node<T>>(start, 0));

        for (Node<T> node : adjList.keySet()) {
            if (node.equals(start)) {
                distances.put(node, 0);
            } else {
                distances.put(node, Integer.MAX_VALUE);
            }
            previousNodes.put(node, null);
        }


        while (!heap.isEmpty()) {

            // getting the smallest unvisited node in distance
            HeapNode<Node<T>> smallestParent = heap.poll();
            Node<T> currentNode = smallestParent.getNode();
            int currentDistance = smallestParent.getValue();


            if (currentNode == null) {
                break;
            }

            if (currentNode.equals(destination)) {

                // preparing the result

                List<Node<T>> path = new ArrayList<>();
                Node<T> startingNode = destination;
                while (startingNode != null) {
                    path.add(startingNode);
                    startingNode = previousNodes.get(startingNode);
                }
                Collections.reverse(path);

                Map<List<Node<T>>, Integer> result = new HashMap<>();
                result.put(path, currentDistance);
                return result;
            }

            // relaxation for each edge in the node
            List<Edge<T>> currentNodeEdges = adjList.get(currentNode);
            for (Edge<T> edge : currentNodeEdges) {
                Node<T> edgeDestination = edge.destination;
                Integer edgeDestinationDistance = distances.get(edgeDestination);
                if (edge.weight + currentDistance < edgeDestinationDistance) {
                    distances.put(edgeDestination, edge.weight + currentDistance);
                    previousNodes.put(edgeDestination, currentNode);
                    heap.add(new HeapNode<>(edgeDestination, edge.weight + currentDistance));
                }

            }


        }
        throw new Exception("No Short path was found");
    }


    public Map<List<Node<T>>, Integer> findShortestRouteLinear(Node<T> start, Node<T> destination) throws Exception {

        List<Node<T>> unvisitedNodes = new ArrayList<>();
        Map<Node<T>, Integer> distances = new HashMap<>();
        Map<Node<T>, Node<T>> previousNodes = new HashMap<>();

        // initialize fields
        for (Node<T> node : adjList.keySet()) {
            if (node.equals(start)) {
                distances.put(node, 0);
            } else {
                distances.put(node, Integer.MAX_VALUE);
            }
            unvisitedNodes.add(node);
            previousNodes.put(node, null);
        }


        while (!unvisitedNodes.isEmpty()) {

            // getting the smallest unvisited node in distance
            Node<T> currentNode = null;
            Integer currentDistance = Integer.MAX_VALUE;

            for (Node<T> node : unvisitedNodes) {
                Integer currentNodeDistance = distances.get(node);
                if (currentNodeDistance < currentDistance) {
                    currentDistance = currentNodeDistance;
                    currentNode = node;
                }
            }

            if (currentNode == null) {
                break;
            }

            if (currentNode.equals(destination)) {

                // preparing the result

                List<Node<T>> path = new ArrayList<>();
                Node<T> startingNode = destination;
                while (startingNode != null) {
                    path.add(startingNode);
                    startingNode = previousNodes.get(startingNode);
                }

                Collections.reverse(path);

                Map<List<Node<T>>, Integer> result = new HashMap<>();
                result.put(path, currentDistance);
                return result;
            }

            // relaxation for each edge in the node
            List<Edge<T>> currentNodeEdges = adjList.get(currentNode);
            for (Edge<T> edge : currentNodeEdges) {
                Node<T> edgeDestination = edge.destination;
                Integer edgeDestinationDistance = distances.get(edgeDestination);
                if (edge.weight + currentDistance < edgeDestinationDistance) {
                    // if the distance is shorter
                    distances.put(edgeDestination, edge.weight + currentDistance);
                    previousNodes.put(edgeDestination, currentNode);
                }

            }

            unvisitedNodes.remove(currentNode);

        }

        throw new Exception("No Short path was found");
    }


}