package algorithms.graph;

import common.CustomGraph;
import common.CustomGraphInput;
import common.Output;

import java.util.ArrayList;
import java.util.Arrays;

public class FloydWarshall {

    public Output floydWarshall(CustomGraphInput input) {
        int src = input.getSrc();
        int target = input.getTarget();
        CustomGraph customGraph = input.getGraph();

        int V = customGraph.numOfNodes;

        int[][] dist = new int[V][V];
        int[][] next = new int[V][V];

        // Initialize distances
        for (int i = 0; i < V; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
            next[i][i] = i;
        }

        // Build distance matrix from CSR adjacency arrays
        for (int u = 0; u < V; u++) {
            for (int i = customGraph.adjOffset[u]; i < customGraph.adjOffset[u + 1]; i++) {
                int v = customGraph.adjTarget[i];
                int weight = customGraph.adjWeight[i];
                dist[u][v] = weight;
                next[u][v] = v;
            }
        }

        // Floyd-Warshall core
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                if (dist[i][k] == Integer.MAX_VALUE)
                    continue;
                for (int j = 0; j < V; j++) {
                    if (dist[k][j] == Integer.MAX_VALUE)
                        continue;
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        // No path exists
        if (dist[src][target] == Integer.MAX_VALUE) {
            return new Output(-1, new ArrayList<>());
        }

        // Reconstruct path
        ArrayList<Integer> path = new ArrayList<>();
        int at = src;
        while (at != target) {
            path.add(at);
            at = next[at][target];
            if (at == -1) {
                return new Output(-1, new ArrayList<>());
            }
        }
        path.add(target);

        return new Output(dist[src][target], path);
    }
}
