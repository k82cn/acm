import java.util.*;

/**
 * Created by dma on 5/3/15.
 */
public class AStarPlanner implements AirelinePlanner {
    private int[][] airpoints;
    //private int[][] visited;

    private List<Path.Edge> target = new ArrayList<Path.Edge>();

    private Path.Edge currentEdge;
    private Path path;

    public AStarPlanner(int[][] airports, List<Path.Edge> targets, int delta) {
        this.airpoints = airports;
        this.target = targets;
        //this.visited = new int[this.airpoints.length][this.airpoints.length];
        path = new Path(delta);
        this.generateInitSet();
    }

    private void generateInitSet() {
        // generate the init set of A* algorithm
        for (int i = 1; i < this.airpoints.length; i++) {
            Path.Edge e = new Path.Edge(0, i, this.airpoints[0][i]);
            e.priority = fn(e);
            this.openSet.add(e);
        }
        this.applyNextStep();
    }

    public void generateOpenSet() {
        for (int i = 0; i < this.airpoints.length; i++) {

            Path.Edge e = new Path.Edge(this.currentEdge.to, i, this.airpoints[this.currentEdge.to][i]);
            e.priority = fn(e);
            this.openSet.add(e);
        }
    }

    public boolean isFinished() {
        for (Path.Edge e : this.target) {
            if(!this.path.contains(e))
                return false;
        }

        return true;
    }



    public void applyNextStep() {
        Path.Edge e;

        do {
            e = this.openSet.poll();
        } while ((this.currentEdge != null && e.from != this.currentEdge.to));

        this.currentEdge = e;

        path.append(this.currentEdge);

    }

    public Path getPath() {
        if (this.isFinished()) {
            path.optimize(this.target, this.airpoints);
            return path;
        }
        return null;
    }


    // Key function & data structure for A*
    private Queue<Path.Edge> openSet = new PriorityQueue<Path.Edge>(128, new Comparator<Path.Edge>() {
        @Override
        public int compare(Path.Edge o1, Path.Edge o2) {
            if (o1.priority > o2.priority)
                return 1;
            else if (o1.priority == o2.priority)
                return 0;
            else
                return -1;
        }
    });

    private int fn(Path.Edge e)
    {
        return gn(e) + hn(e);
    }

    private int gn(Path.Edge e)
    {
        return this.path.getCost();
    }

    private int hn(Path.Edge e)
    {
        return e.cost + this.arrayMin(e);
    }

    private int arrayMin(Path.Edge e) {

        // if e is target path, then expanded cost is zero
        for (Path.Edge edge : target)
            if (e.equals(edge))
                return 0;

        // if e is not target path, expand path with low cost in Dijkstra
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < airpoints.length; i++) {
            if (i == e.to) continue;

                min = Math.min(min, airpoints[e.to][i]);

        }

        return min;
    }
}
