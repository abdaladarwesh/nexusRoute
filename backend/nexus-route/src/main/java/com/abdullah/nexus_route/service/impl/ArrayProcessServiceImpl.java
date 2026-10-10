package com.abdullah.nexus_route.service.impl;

import com.abdullah.nexus_route.service.ArrayProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ArrayProcessServiceImpl implements ArrayProcessService {
    private final Executor taskExecutor;

    @Override
    public <T> CompletableFuture<Void> processArrayAsync(List<T> array, int numberOfChunks, Consumer<List<T>> task) {


        List<List<T>> nodesPartitions = partitionList(array, numberOfChunks);
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (List<T> partition : nodesPartitions) {
            tasks.add(
                    CompletableFuture.runAsync(() -> task.accept(partition), taskExecutor)
            );
        }

        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));
    }

    @Override
    public <T> List<List<T>> partitionList(List<T> list, int numPartitions) {
        List<List<T>> partitions = new ArrayList<>();
        int size = list.size();
        int chunkSize = (int) Math.ceil((double) size / numPartitions);

        for (int i = 0; i < size; i += chunkSize) {
            partitions.add(list.subList(i, Math.min(i + chunkSize, size)));
        }
        return partitions;
    }
}
