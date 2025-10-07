package algorithms;

import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BellmanFord {

    public static Output bellmanFord(Input input) {

        // Set source and target vertices index
        int target = input.getTarget();
        int src = input.getSrc();
        Graph<Integer, DefaultWeightedEdge> graph = input.getGraph();

        // Set number of vertices
        int V = graph.vertexSet().size();

        // Initialize distance array; Fill every with MAX_VALUE except for 1st
        int[] dist = new int[V];
        int[] prev = new int[V]; // here for easier path reconstruction
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        // CORE IDEA: In a graph with V vertices, the shortest path can have at most V-1 edges.
        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            for (DefaultWeightedEdge edge : graph.edgeSet()) {
                int u = graph.getEdgeSource(edge); // shortest distance to u
                int v = graph.getEdgeTarget(edge); // shortest distance to v
                int weight = (int) graph.getEdgeWeight(edge);

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                }
                // Reverse the previous check
                if (dist[v] != Integer.MAX_VALUE && dist[v] + weight < dist[u]) {
                    dist[u] = dist[v] + weight;
                    prev[u] = v;
                }
            }
        }

        // Negative cycle check
        // If after V-1 checks you can still find shorter path --> graph has negative cycle
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            int u = graph.getEdgeSource(edge);
            int v = graph.getEdgeTarget(edge);
            int weight = (int) graph.getEdgeWeight(edge);

            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                System.out.println("Graph contains a negative weight cycle!");
                return new Output(-1, new ArrayList<>());
            }
        }

        // This is for handling unreachable nodes
        if (dist[target] == Integer.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        // Reverse the path
        ArrayList<Integer> shortestPath = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            shortestPath.add(at);
        }
        Collections.reverse(shortestPath);

        return new Output(dist[target], shortestPath);
    }
}
