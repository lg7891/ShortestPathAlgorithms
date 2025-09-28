package algorithms;

import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BellmanFord {

    public static Output bellmanFord(Graph<Integer, DefaultWeightedEdge> graph) {
        int V = graph.vertexSet().size();
        int src = 0;
        int target = V - 1;

        int[] dist = new int[V];
        int[] prev = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            for (DefaultWeightedEdge edge : graph.edgeSet()) {
                int u = graph.getEdgeSource(edge);
                int v = graph.getEdgeTarget(edge);
                int weight = (int) graph.getEdgeWeight(edge);

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                }
                if (dist[v] != Integer.MAX_VALUE && dist[v] + weight < dist[u]) {
                    dist[u] = dist[v] + weight;
                    prev[u] = v;
                }
            }
        }

        // Negative cycle check
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            int u = graph.getEdgeSource(edge);
            int v = graph.getEdgeTarget(edge);
            int weight = (int) graph.getEdgeWeight(edge);

            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                System.out.println("Graph contains a negative weight cycle!");
                return new Output(-1, new ArrayList<>());
            }
        }

        if (dist[target] == Integer.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        ArrayList<Integer> shortestPath = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            shortestPath.add(at);
        }
        Collections.reverse(shortestPath);

        return new Output(dist[target], shortestPath);
    }
}
