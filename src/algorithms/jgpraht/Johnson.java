package algorithms.jgpraht;

import common.JGraphTInput;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class Johnson {

    public static Output johnson(JGraphTInput input) {
        int target = input.getTarget();
        int src = input.getSrc();
        Graph<Integer, DefaultWeightedEdge> graph = input.getGraph();

        // Create vertex-to-index mapping
        Map<Integer, Integer> vertexToIndex = new HashMap<>();
        Map<Integer, Integer> indexToVertex = new HashMap<>();
        int index = 0;
        for (Integer vertex : graph.vertexSet()) {
            vertexToIndex.put(vertex, index);
            indexToVertex.put(index, vertex);
            index++;
        }

        int V = graph.vertexSet().size();

        // Step 1: Build edge list for Bellman-Ford (with mapped indices)
        List<int[]> edges = new ArrayList<>();

        // Use edgeSet() to process each edge only once
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            Integer u = graph.getEdgeSource(edge);
            Integer v = graph.getEdgeTarget(edge);

            // Skip self-loops
            if (u.equals(v)) continue;

            int uIdx = vertexToIndex.get(u);
            int vIdx = vertexToIndex.get(v);
            int weight = (int) graph.getEdgeWeight(edge);

            // For undirected graphs, add both directions
            edges.add(new int[]{uIdx, vIdx, weight});
            edges.add(new int[]{vIdx, uIdx, weight});
        }

        // Step 2: Run Bellman-Ford from new vertex (V)
        int[] h = bellmanFord(edges, V);

        if (h == null) {
            System.out.println("Negative weight cycle detected in Johnson's algorithm.");
            return new Output(-1, new ArrayList<>());
        }

        // Step 3: Reweight edges using ORIGINAL vertex IDs
        Graph<Integer, DefaultWeightedEdge> reweightedGraph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices with original IDs
        for (Integer vertex : graph.vertexSet()) {
            reweightedGraph.addVertex(vertex);
        }

        // Add reweighted edges - use edgeSet() to avoid duplicates
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            Integer u = graph.getEdgeSource(edge);
            Integer v = graph.getEdgeTarget(edge);

            // Skip self-loops
            if (u.equals(v)) continue;

            int uIdx = vertexToIndex.get(u);
            int vIdx = vertexToIndex.get(v);
            int weight = (int) graph.getEdgeWeight(edge);
            int newWeight = weight + h[uIdx] - h[vIdx];

            // Add edge (SimpleWeightedGraph handles undirected automatically)
            DefaultWeightedEdge newEdge = reweightedGraph.addEdge(u, v);
            if (newEdge != null) {
                reweightedGraph.setEdgeWeight(newEdge, newWeight);
            }
        }

        // Step 4: Run Dijkstra on reweighted graph
        Output reweightedOutput = DijkstraBinaryHeap.dijkstraBinaryHeap(
                new JGraphTInput(src, target, reweightedGraph)
        );

        // Step 5: Adjust final path cost to original weights
        if (reweightedOutput.getTotalPathPrice() == -1) {
            return new Output(-1, new ArrayList<>());
        }

        int srcIdx = vertexToIndex.get(src);
        int targetIdx = vertexToIndex.get(target);
        int correctedCost = reweightedOutput.getTotalPathPrice() + h[targetIdx] - h[srcIdx];

        return new Output(correctedCost, reweightedOutput.getShortestPath());
    }

    // Bellman-Ford algorithm for reweighting
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
            boolean changed = false;
            for (int[] edge : allEdges) {
                int u = edge[0], v = edge[1], weight = edge[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    changed = true;
                }
            }
            if (!changed) break; // Early exit optimization
        }

        // Check for negative weight cycles
        for (int[] edge : allEdges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                return null; // Negative cycle detected
            }
        }

        return Arrays.copyOf(dist, V); // Drop the extra node
    }
}
