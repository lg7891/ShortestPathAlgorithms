package algorithms.jgpraht;

import common.JGraphTInput;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BellmanFord {

    public static Output bellmanFord(JGraphTInput input) {
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
        long[] dist = new long[V];
        int[] prev = new int[V];

        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(prev, -1);

        // Map src and target to indices
        int srcIdx = vertexToIndex.get(src);
        int targetIdx = vertexToIndex.get(target);

        dist[srcIdx] = 0;

        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            boolean changed = false;

            for (DefaultWeightedEdge edge : graph.edgeSet()) {
                Integer u = graph.getEdgeSource(edge);
                Integer v = graph.getEdgeTarget(edge);
                long weight = (long) graph.getEdgeWeight(edge);

                int uIdx = vertexToIndex.get(u);
                int vIdx = vertexToIndex.get(v);

                // Relax u -> v
                if (dist[uIdx] != Long.MAX_VALUE && dist[uIdx] + weight < dist[vIdx]) {
                    dist[vIdx] = dist[uIdx] + weight;
                    prev[vIdx] = uIdx;
                    changed = true;
                }

                // Relax v -> u (undirected graph)
                if (dist[vIdx] != Long.MAX_VALUE && dist[vIdx] + weight < dist[uIdx]) {
                    dist[uIdx] = dist[vIdx] + weight;
                    prev[uIdx] = vIdx;
                    changed = true;
                }
            }

            // Early termination if no changes
            if (!changed) break;
        }

        // Check if target is reachable
        if (dist[targetIdx] == Long.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        // Reconstruct path using original vertex IDs
        ArrayList<Integer> shortestPath = new ArrayList<>();
        for (int at = targetIdx; at != -1; at = prev[at]) {
            shortestPath.add(indexToVertex.get(at));
        }
        Collections.reverse(shortestPath);

        return new Output((int) dist[targetIdx], shortestPath);
    }
}