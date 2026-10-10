package com.abdullah.nexus_route.model.Graph;

public class Edge<T> {
    Node<T> destination;
    int weight;

    public Edge(Node<T> destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}
