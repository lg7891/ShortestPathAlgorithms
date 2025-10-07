package algorithms;

import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class DijkstraBinaryHeap {

    public static Output dijkstraBinaryHeap(Input input) {

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

        // PriorityQueue to store vertices that need to be processed (pairs of [distance, node])
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        priorityQueue.offer(new int[]{0, src}); // offer() is basically add()

        // While there is something to visit
        while (!priorityQueue.isEmpty()) {

            // TODO: check this, simplify if possible
            int[] current =  priorityQueue.poll(); // Get the closest node (poll() returns the smallest distance
            int d = current[0]; // Distance value
            int u  = current[1]; // Node index

            // Node can be added more times that is why we skip it
            // Only the smallest distance is valid
            if (d > dist[u]) continue;

            // For each edge connected to u find a neighbour
            for (DefaultWeightedEdge edge : graph.edgesOf(u)) {
                int v = graph.getEdgeSource(edge).equals(u)
                        ? graph.getEdgeTarget(edge)
                        : graph.getEdgeSource(edge); // Because it is undirected we use ?:

                double weight = graph.getEdgeWeight(edge); // Edge cost/weight

                // if going through u gives shorter path to v, update the queue
                if (dist[v] > dist[u] + weight) {
                    dist[v] = (int)(dist[u] + weight);
                    prev[v] = u;
                    priorityQueue.offer(new int[]{dist[v], v});
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
