package common;

public class Input {
    int src;
    int   target;
    Graph graph;

    public Input(int src, int target, Graph graph) {
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

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
