package algorithms;

import common.Graph;
import common.Input;
import common.Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Johnson {

    public Output johnson(Input input) {
        int src = input.getSrc();
        int target = input.getTarget();
        Graph graph = input.getGraph();

        int V = graph.numOfNodes;

        // Step 1: Build edge list for Bellman-Ford
        List<int[]> edges = new ArrayList<>();
        for (int u = 0; u < V; u++) {
            for (int i = graph.adjOffset[u]; i < graph.adjOffset[u + 1]; i++) {
                int v = graph.adjTarget[i];
                int weight = graph.adjWeight[i];
                // Only add each edge once (CSR stores both directions already)
                if (u < v) {
                    edges.add(new int[] { u, v, weight });
                    edges.add(new int[] { v, u, weight });
                }
            }
        }

        // Step 2: Run Bellman-Ford from virtual node to get h[] reweighting values
        int[] h = bellmanFord(edges, V);

        if (h == null) {
            System.out.println("Negative weight cycle detected in Johnson's algorithm.");
            return new Output(-1, new ArrayList<>());
        }

        // Step 3: Build reweighted graph using CSR
        int[] reweightedOffset = Arrays.copyOf(graph.adjOffset, graph.adjOffset.length);
        int[] reweightedTarget = Arrays.copyOf(graph.adjTarget, graph.adjTarget.length);
        int[] reweightedWeight = new int[graph.adjWeight.length];

        for (int u = 0; u < V; u++) {
            for (int i = graph.adjOffset[u]; i < graph.adjOffset[u + 1]; i++) {
                int v = graph.adjTarget[i];
                int weight = graph.adjWeight[i];
                reweightedWeight[i] = weight + h[u] - h[v];
            }
        }

        Graph reweightedGraph = new Graph(V, graph.numOfEdges, reweightedOffset, reweightedTarget, reweightedWeight);

        // Step 4: Run Dijkstra on reweighted graph
        Output reweightedOutput = dijkstraBinaryHeap(
                new Input(src, target, reweightedGraph)
        );

        // Step 5: Adjust final path cost back to original weights
        if (reweightedOutput.getTotalPathPrice() == -1) {
            return new Output(-1, new ArrayList<>());
        }

        int correctedCost = reweightedOutput.getTotalPathPrice() + h[target] - h[src];

        return new Output(correctedCost, reweightedOutput.getShortestPath());
    }

    private static int[] bellmanFord(List<int[]> edges, int V) {
        int[] dist = new int[V + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[V] = 0; // Virtual source node

        List<int[]> allEdges = new ArrayList<>(edges);
        for (int i = 0; i < V; i++) {
            allEdges.add(new int[] { V, i, 0 });
        }

        for (int i = 0; i < V; i++) {
            boolean changed = false;
            for (int[] edge : allEdges) {
                int u = edge[0], v = edge[1], weight = edge[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    changed = true;
                }
            }
            if (!changed)
                break;
        }

        // Negative cycle detection
        for (int[] edge : allEdges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                return null;
            }
        }

        return Arrays.copyOf(dist, V);
    }

    private static Output dijkstraBinaryHeap(Input input) {

        int src = input.getSrc();
        int target = input.getTarget();
        Graph graph = input.getGraph();

        int V = graph.numOfNodes;

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        int[] prev = new int[V];
        Arrays.fill(prev, -1);

        boolean[] visited = new boolean[V];

        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[] { src, 0 });

        while (!pq.isEmpty()) {

            int[] cur = pq.poll();
            int u = cur[0];

            if (visited[u])
                continue;
            visited[u] = true;

            if (u == target)
                break;

            for (int i = graph.adjOffset[u]; i < graph.adjOffset[u + 1]; i++) {

                int v = graph.adjTarget[i];
                int weight = graph.adjWeight[i];

                if (visited[v])
                    continue;

                int newDist = dist[u] + weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    prev[v] = u;
                    pq.offer(new int[] { v, newDist });
                }
            }
        }

        if (dist[target] == Integer.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        ArrayList<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);

        return new Output(dist[target], path);
    }
}
