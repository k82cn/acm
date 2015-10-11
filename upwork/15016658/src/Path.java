import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dma on 5/2/15.
 */
public class Path implements Iterable<Path.Edge> {

    private List<Edge> edges = new ArrayList<Edge>();

    private int cost;
    private Map<Integer, String> cityNames;
    private int count;

    public Path(int deta) {
        this.cost = -deta;
    }

    @Override
    public Iterator<Edge> iterator() {
        return this.edges.iterator();
    }

    public void append(Edge edge) {
        // We need to merge page here

        this.edges.add(edge);
        this.cost = this.cost + edge.cost;
        this.count = this.count + 1;
    }

    public int getCost() {
        return cost;
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(this.count + " nodes expanded\n");
        sb.append("cost = " + getCost() + "\n");
        for (Path.Edge edge : this.edges) {
            sb.append(edge.getString(this.cityNames)).append("\n");
        }
        return sb.toString();
    }

    public void optimize(List<Edge> pinEdges, int[][] costs) {
        List<Edge> res = new ArrayList<Edge>();
        for (Edge e : this.edges) {
            if (pinEdges.contains(e) || res.isEmpty()) {
                res.add(e);
                continue;
            }

            Edge lastEdge = res.remove(res.size() - 1);
            if (pinEdges.contains(lastEdge)) {
                // add it back
                res.add(lastEdge);
                res.add(e);
            } else {
                res.add(new Edge(lastEdge.from, e.to, costs[lastEdge.from][e.to]));
                this.cost = this.cost - lastEdge.cost - e.cost + costs[lastEdge.from][e.to];
            }
        }
        this.edges = res;

    }

    public Map<Integer, String> getCityNames() {
        return cityNames;
    }

    public void setCityNames(Map<Integer, String> cityNames) {
        this.cityNames = cityNames;
    }

    public boolean contains(Edge e) {
        return this.edges.contains(e);
    }

    public static class Edge {
        public int from;
        public int to;
        public int cost;
        public int priority;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        public Edge() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (from != edge.from) return false;
            return to == edge.to;

        }

        @Override
        public int hashCode() {
            int result = from;
            result = 31 * result + to;
            return result;
        }

        @Override
        public String toString() {
            return "Flight " + from + " to " + to;
        }

        public String getString(Map<Integer, String> cityNames) {
            if (cityNames != null)
                return "Flight " + cityNames.get(from) + " to " + cityNames.get(to);
            else
                return "Flight " + from + " to " + to;
        }
    }

}
