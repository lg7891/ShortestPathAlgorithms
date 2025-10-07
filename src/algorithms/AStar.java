package algorithms;

import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class AStar {

    public static Output aStar(Input input) {

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

        // Min-heap by f = g + h
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        priorityQueue.add(new int[]{src, heuristic(src, target)}); // node, f-cost

        // While there is something to visit
        while (!priorityQueue.isEmpty()) {
            int u = priorityQueue.poll()[0]; // Node index
            if (u == target) break; // found shortest path

            // Relax neighbours
            for (DefaultWeightedEdge edge : graph.edgesOf(u)) {
                int v = Graphs.getOppositeVertex(graph, edge, u);
                int weight = (int) graph.getEdgeWeight(edge);

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;     // update g(v)
                    prev[v] = u;                    // set predecessor
                    int f = dist[v] + heuristic(v, target);
                    priorityQueue.add(new int[]{v, f});   // add new candidate
                }
            }
        }

        // Reconstruct path
        ArrayList<Integer> shortestPath = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            shortestPath.add(at);
        }
        Collections.reverse(shortestPath);

        return new Output(dist[target], shortestPath);
    }

    // Simple heuristic (Euclidean or Manhattan depending on graph meaning)
    private static int heuristic(int u, int v) {
        return 0; // if abstract graph with no coordinates, you can just return 0 -> falls back to Dijkstra
    }
}
