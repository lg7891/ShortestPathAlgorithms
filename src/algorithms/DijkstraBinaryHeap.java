package algorithms;

import common.Input;
import common.Graph;
import common.Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DijkstraBinaryHeap {

    public static Output dijkstraBinaryHeap(Input input) {

        int src = input.getSrc();
        int target = input.getTarget();
        Graph graph = input.getGraph();

        int V = graph.numOfNodes;

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        int[] prev = new int[V];
        Arrays.fill(prev, -1);

        boolean[] visited = new boolean[V];

        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{src, 0});

        while (!pq.isEmpty()) {

            int[] cur = pq.poll();
            int u = cur[0];

            if (visited[u]) continue;
            visited[u] = true;

            if (u == target) break;

            for (int i = graph.adjOffset[u]; i < graph.adjOffset[u + 1]; i++) {

                int v = graph.adjTarget[i];
                int weight = graph.adjWeight[i];

                if (visited[v]) continue;

                int newDist = dist[u] + weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    prev[v] = u;
                    pq.offer(new int[]{v, newDist});
                }
            }
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
