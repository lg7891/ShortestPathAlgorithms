package algorithms.graph;

import common.CustomGraph;
import common.CustomGraphInput;
import common.Output;
import org.jheaps.AddressableHeap;
import org.jheaps.tree.FibonacciHeap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DijkstraFibonacciHeap {

    public Output dijkstraFibonacciHeap(CustomGraphInput input) {
        int src = input.getSrc();
        int target = input.getTarget();
        CustomGraph customGraph = input.getGraph();

        int V = customGraph.numOfNodes;

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        int[] prev = new int[V];
        Arrays.fill(prev, -1);

        dist[src] = 0;

        AddressableHeap<Integer, Integer> fibHeap = new FibonacciHeap<>();
        Map<Integer, AddressableHeap.Handle<Integer, Integer>> handles = new HashMap<>();
        handles.put(src, fibHeap.insert(0, src));

        while (!fibHeap.isEmpty()) {
            AddressableHeap.Handle<Integer, Integer> minHandle = fibHeap.deleteMin();
            int u = minHandle.getValue();
            int currentDist = minHandle.getKey();

            // Skip stale heap entries
            if (currentDist > dist[u])
                continue;

            if (u == target)
                break;

            for (int i = customGraph.adjOffset[u]; i < customGraph.adjOffset[u + 1]; i++) {
                int v = customGraph.adjTarget[i];
                int weight = customGraph.adjWeight[i];

                int newDist = dist[u] + weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    prev[v] = u;
                    if (handles.containsKey(v)) {
                        handles.get(v).decreaseKey(newDist);
                    } else {
                        handles.put(v, fibHeap.insert(newDist, v));
                    }
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
