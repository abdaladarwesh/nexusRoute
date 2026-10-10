package com.abdullah.nexus_route.service;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface ArrayProcessService {
    <T> List<List<T>> partitionList(List<T> list, int numPartitions);
    <T> CompletableFuture<Void> processArrayAsync(List<T> array, int numberOfChunks, Consumer<List<T>> task);
}
