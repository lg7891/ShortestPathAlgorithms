package algorithms;

import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;

public class Johnson {

    public static Output johnson(Graph<Integer, DefaultWeightedEdge> graph) {
        // Set number of vertices
        int V = graph.vertexSet().size();
        // Set source and target vertices index
        int target = V - 1;
        int src = 0;

        // Step 1: Build edge list for Bellman-Ford
        List<int[]> edges = new ArrayList<>();
        for (int u : graph.vertexSet()) {
            for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(u)) {
                int v = graph.getEdgeTarget(edge);
                double weight = graph.getEdgeWeight(edge);
                edges.add(new int[]{u, v, (int) weight});
            }
        }

        // Step 2: Run Bellman-Ford from new vertex (V)
        int[] h = bellmanFord(edges, V); // h[i] is the potential of vertex i

        if (h == null) {
            // Negative weight cycle detected
            return new Output(Integer.MAX_VALUE, new ArrayList<>());
        }

        // Step 3: Reweight edges
        Graph<Integer, DefaultWeightedEdge> reweightedGraph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (int i = 0; i < V; i++) {
            reweightedGraph.addVertex(i);
        }

        for (int u : graph.vertexSet()) {
            for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(u)) {
                int v = graph.getEdgeTarget(edge);

                if (u == v) continue; // Skip self-loops

                int weight = (int) graph.getEdgeWeight(edge);
                int newWeight = weight + h[u] - h[v];

                DefaultWeightedEdge newEdge = reweightedGraph.addEdge(u, v);
                if (newEdge != null) {
                    reweightedGraph.setEdgeWeight(newEdge, newWeight);
                }
            }
        }

        // Step 4: Run Dijkstra on reweighted graph
        Output reweightedOutput = DijkstraBinaryHeap.dijkstraBinaryHeap(reweightedGraph);

        // Step 5: Adjust final path cost to original weights
        int correctedCost = reweightedOutput.getTotalPathPrice() + h[target] - h[src];
        return new Output(correctedCost, reweightedOutput.getShortestPath());
    }

    // Bellman-Ford algorithm for reweighting (returns h[] potentials or null if negative cycle)
    private static int[] bellmanFord(List<int[]> edges, int V) {
        int[] dist = new int[V + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[V] = 0; // New virtual source

        // Add zero-weight edges from new node V to all others
        List<int[]> allEdges = new ArrayList<>(edges);
        for (int i = 0; i < V; i++) {
            allEdges.add(new int[]{V, i, 0});
        }

        // Relax all edges V times
        for (int i = 0; i < V; i++) {
            for (int[] edge : allEdges) {
                int u = edge[0], v = edge[1], weight = edge[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
            }
        }

        // Check for negative weight cycles
        for (int[] edge : allEdges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                System.out.println("Negative weight cycle detected.");
                return null;
            }
        }

        return Arrays.copyOf(dist, V); // Drop the extra node
    }
}
