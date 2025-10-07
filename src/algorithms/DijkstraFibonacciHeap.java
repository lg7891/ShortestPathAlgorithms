package algorithms;

import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jheaps.AddressableHeap;
import org.jheaps.tree.FibonacciHeap;

import java.util.*;

public class DijkstraFibonacciHeap {

    public static Output dijkstraFibonacciHeap(Input input) {

        // Set source and target vertices index
        int target = input.getTarget();
        int src = input.getSrc();
        Graph<Integer, DefaultWeightedEdge> graph = input.getGraph();

        // Set number of vertices
        int V = graph.vertexSet().size();

        // Initialize distance array; Fill every with MAX_VALUE except for 1st
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // Storing previous nodes; Create Array for previously visited vertices
        int[] prev = new int[V];
        Arrays.fill(prev, -1);

        // JHeaps FibonacciHeap: key = distance, value = vertex
        AddressableHeap<Double, Integer> fibHeap = new FibonacciHeap<>();
        Map<Integer, AddressableHeap.Handle<Double, Integer>> heapHandles = new HashMap<>();

        // Insert all vertices with initial distances
        for (int v : graph.vertexSet()) {
            AddressableHeap.Handle<Double, Integer> handle =
                    fibHeap.insert((double) dist[v], v);
            heapHandles.put(v, handle);
        }

        // While there is something to visit
        while (!fibHeap.isEmpty()) {
            AddressableHeap.Handle<Double, Integer> minHandle = fibHeap.deleteMin();
            int u = minHandle.getValue(); // Node index

            // For each edge connected to u find a neighbour
            for (DefaultWeightedEdge edge : graph.edgesOf(u)) {
                int v = graph.getEdgeSource(edge).equals(u)
                        ? graph.getEdgeTarget(edge)
                        : graph.getEdgeSource(edge); // Because it is undirected we use ?:

                double weight = graph.getEdgeWeight(edge); // Edge cost/weight

                // if going through u gives shorter path to v, update the queue
                if (dist[u] != Integer.MAX_VALUE && dist[v] > dist[u] + weight) {
                    dist[v] = (int) (dist[u] + weight);
                    prev[v] = u;
                    heapHandles.get(v).decreaseKey((double) dist[v]);
                }
            }
        }

        // Reconstruct shortest path
        ArrayList<Integer> shortestPath = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            shortestPath.add(at);
        }
        Collections.reverse(shortestPath);

        return new Output(dist[target], shortestPath);
    }
}
