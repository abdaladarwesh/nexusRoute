package com.abdullah.nexus_route.model.Graph;

public class GraphEdge<T> {
    private GraphNode<T> destination;
    private double weight;

    public GraphNode<T> getDestination() {
        return destination;
    }

    public void setDestination(GraphNode<T> destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public GraphEdge(GraphNode<T> destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }
}
