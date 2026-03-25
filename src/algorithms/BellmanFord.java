package algorithms;

import common.Graph;
import common.Input;
import common.Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BellmanFord {

    public Output bellmanFord(Input input) {
        int src = input.getSrc();
        int target = input.getTarget();
        Graph graph = input.getGraph();

        int V = graph.numOfNodes;

        int[] dist = new int[V];
        int[] prev = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);

        dist[src] = 0;

        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            boolean changed = false;

            for (int u = 0; u < V; u++) {
                if (dist[u] == Integer.MAX_VALUE) continue;

                for (int j = graph.adjOffset[u]; j < graph.adjOffset[u + 1]; j++) {
                    int v = graph.adjTarget[j];
                    int weight = graph.adjWeight[j];

                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        prev[v] = u;
                        changed = true;
                    }
                }
            }

            if (!changed) break;
        }

        if (dist[target] == Integer.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        ArrayList<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);

        return new Output(dist[target], path);
    }
}
