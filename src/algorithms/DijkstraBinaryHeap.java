package algorithms;

import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class DijkstraBinaryHeap {

    public static Output dijkstraBinaryHeap(Graph<Integer, DefaultWeightedEdge> graph) {
        int V = graph.vertexSet().size();
        int target = V - 1;
        int src = 0;

//        Initialize distance array
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

//        Storing previous nodes
        int[] prev = new int[V];
        Arrays.fill(prev, -1);

//        PriorityQueue to store vertices that need to be processed (pairs of [distance, node])
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        priorityQueue.offer(new int[]{0, src}); // offer() is basically add()

        while (!priorityQueue.isEmpty()) {
            int[] current =  priorityQueue.poll();
            int d = current[0];
            int u  = current[1];

            if (d > dist[u]) continue;

            for (DefaultWeightedEdge edge : graph.edgesOf(u)) {
                int v = graph.getEdgeSource(edge).equals(u)
                        ? graph.getEdgeTarget(edge)
                        : graph.getEdgeSource(edge);

                double weight = graph.getEdgeWeight(edge);

                if (dist[v] > dist[u] + weight) {
                    dist[v] = (int)(dist[u] + weight);
                    prev[v] = u;
                    priorityQueue.offer(new int[]{dist[v], v});
                }
            }
        }

        ArrayList<Integer> shortestPath = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            shortestPath.add(at);
        }
        Collections.reverse(shortestPath);

        return new Output(dist[target], shortestPath);
    }
}
