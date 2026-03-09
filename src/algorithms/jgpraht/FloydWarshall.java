package algorithms.jgpraht;

import common.JGraphTInput;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FloydWarshall {

    public static Output floydWarshall(JGraphTInput input) {
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

        // Distance matrix
        int[][] dist = new int[V][V];
        int[][] next = new int[V][V];

        // Initialize distances
        for (int i = 0; i < V; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
            next[i][i] = i;
        }

        // Convert graph to adjacency matrix using mapped indices
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            Integer u = graph.getEdgeSource(edge);
            Integer v = graph.getEdgeTarget(edge);
            int w = (int) graph.getEdgeWeight(edge);

            int uIdx = vertexToIndex.get(u);
            int vIdx = vertexToIndex.get(v);

            // Undirected graph
            dist[uIdx][vIdx] = w;
            dist[vIdx][uIdx] = w;
            next[uIdx][vIdx] = vIdx;
            next[vIdx][uIdx] = uIdx;
        }

        // Floyd-Warshall core algorithm
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE &&
                            dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        // Map src and target to indices
        int srcIdx = vertexToIndex.get(src);
        int targetIdx = vertexToIndex.get(target);

        // No path exists
        if (next[srcIdx][targetIdx] == -1 || dist[srcIdx][targetIdx] == Integer.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        // Reconstruct path using original vertex IDs
        ArrayList<Integer> shortestPath = new ArrayList<>();
        int at = srcIdx;
        while (at != targetIdx) {
            shortestPath.add(indexToVertex.get(at));  // Convert back to original ID
            at = next[at][targetIdx];

            // Safety check for infinite loops
            if (at == -1) {
                return new Output(-1, new ArrayList<>());
            }
        }
        shortestPath.add(indexToVertex.get(targetIdx));  // Add final vertex

        int totalPathPrice = dist[srcIdx][targetIdx];

        return new Output(totalPathPrice, shortestPath);
    }
}
