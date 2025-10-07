package common;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Input {
    int src;
    int target;
    Graph<Integer, DefaultWeightedEdge> graph;

    public Input(int src, int target, Graph<Integer, DefaultWeightedEdge> graph) {
        this.src = src;
        this.target = target;
        this.graph = graph;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Graph<Integer, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    public void setGraph(Graph<Integer, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }
}
