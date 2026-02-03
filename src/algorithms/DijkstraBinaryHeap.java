package algorithms;

import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class DijkstraBinaryHeap {

    public static Output dijkstraBinaryHeap(Input input) {
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

        double[] dist = new double[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);

        int[] prev = new int[V];
        Arrays.fill(prev, -1);

        boolean[] visited = new boolean[V];

        // Map src and target to indices
        int srcIdx = vertexToIndex.get(src);
        int targetIdx = vertexToIndex.get(target);

        dist[srcIdx] = 0;

        // PriorityQueue: [distance, vertexIndex]
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        pq.offer(new double[]{0, srcIdx});

        while (!pq.isEmpty()) {
            double[] cur = pq.poll();
            double d = cur[0];
            int uIdx = (int) cur[1];

            // Skip if already visited (optimization)
            if (visited[uIdx]) continue;
            visited[uIdx] = true;

            // Get original vertex ID
            Integer u = indexToVertex.get(uIdx);

            // UNDIRECTED: Explore ALL edges connected to u
            for (DefaultWeightedEdge edge : graph.edgesOf(u)) {
                // Get the neighbor (could be source or target)
                Integer neighbor = graph.getEdgeSource(edge);
                if (neighbor.equals(u)) {
                    neighbor = graph.getEdgeTarget(edge);
                }

                int vIdx = vertexToIndex.get(neighbor);

                // Skip if already visited
                if (visited[vIdx]) continue;

                double weight = graph.getEdgeWeight(edge);
                double newDist = dist[uIdx] + weight;

                if (newDist < dist[vIdx]) {
                    dist[vIdx] = newDist;
                    prev[vIdx] = uIdx;
                    pq.offer(new double[]{newDist, vIdx});
                }
            }
        }

        // Check if target is reachable
        if (dist[targetIdx] == Double.POSITIVE_INFINITY) {
            return new Output(-1, new ArrayList<>());
        }

        // Reconstruct path using original vertex IDs
        ArrayList<Integer> path = new ArrayList<>();
        for (int at = targetIdx; at != -1; at = prev[at]) {
            path.add(indexToVertex.get(at));
        }
        Collections.reverse(path);

        return new Output((int) dist[targetIdx], path);
    }
}
