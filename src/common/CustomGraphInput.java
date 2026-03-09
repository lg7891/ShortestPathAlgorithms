package common;

public class CustomGraphInput {
    int src;
    int target;
    CustomGraph customGraph;

    public CustomGraphInput(int src, int target, CustomGraph customGraph) {
        this.src = src;
        this.target = target;
        this.customGraph = customGraph;
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

    public CustomGraph getGraph() {
        return customGraph;
    }

    public void setGraph(CustomGraph customGraph) {
        this.customGraph = customGraph;
    }
}
