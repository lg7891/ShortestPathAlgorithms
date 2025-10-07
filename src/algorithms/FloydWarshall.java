package algorithms;

import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Arrays;

public class FloydWarshall {

    public static Output floydWarshall(Input input) {

        // Set source and target vertices index
        int target = input.getTarget();
        int src = input.getSrc();
        Graph<Integer, DefaultWeightedEdge> graph = input.getGraph();

        // Set number of vertices
        int V = graph.vertexSet().size();

        // Distance matrix
        int[][] dist = new int[V][V]; // Shortest known distance from i to j
        int[][] next = new int[V][V]; // Next-hop matrix for path reconstruction

        // Set all distances to INF and distance to itself set to 0
        for (int i = 0; i < V; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
            next[i][i] = i;
        }

        // Converting graph to adjacency matrix
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            int u = graph.getEdgeSource(edge);
            int v = graph.getEdgeTarget(edge);
            int w = (int) graph.getEdgeWeight(edge);

            dist[u][v] = w;
            dist[v][u] = w; // undirected
            next[u][v] = v;
            next[v][u] = u;
        }

        // CORE: Triple loop
        // Floyd–Warshall with path reconstruction
        for (int k = 0; k < V; k++) { // Intermediate node k
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    // Consider if path i -> k -> j is better than i -> j
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        // Update dist and next
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        ArrayList<Integer> shortestPath = new ArrayList<>();

        // No path exists
        if (next[src][target] == -1) {
            return new Output(Integer.MAX_VALUE, shortestPath);
        }

        // Reconstruct path
        int at = src;
        while (at != target) {
            shortestPath.add(at);
            at = next[at][target];
        }
        shortestPath.add(target);

        int totalPathPrice = dist[src][target];

        return new Output(totalPathPrice, shortestPath);
    }
}
