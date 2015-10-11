import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by dma on 5/2/15.
 */
public class FlightScheduler {
    // init the Strategy class
    AirelinePlanner planner;
    private int[][] airports;
    private int delta;
    Map<Integer, String> cityNames = new HashMap<Integer, String>();
    private List<Path.Edge> targets = new ArrayList<Path.Edge>();

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java FlatScheduler input.txt");
            return;
        }

        FlightScheduler scheduler = new FlightScheduler();

        scheduler.readInput(args[0]);
        Path path = scheduler.airelinePlan();

        System.out.println(path);
    }

    public void readInput(String args) {
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(args));
            int cityIdx = 0;
            List<Integer> cities = new ArrayList<Integer>();
            Map<String, Integer> cityIds = new HashMap<String, Integer>();
            List<Path.Edge> airs = new ArrayList<Path.Edge>();

            while (sc.hasNext()) {
                String line = sc.nextLine();
                int idx = line.indexOf('#');
                if (idx >= 0)
                    line = line.substring(0, idx);
                String[] items = line.split(" ");

                if (items[0].equals("City")) {
                    cities.add(Integer.parseInt(items[2]));
                    cityNames.put(cityIdx, items[1]);
                    cityIds.put(items[1], cityIdx);
                    cityIdx++;
                } else if (items[0].equals("Time")) {
                    int i = cityIds.get(items[1]);
                    int j = cityIds.get(items[2]);

                    airs.add(new Path.Edge(i, j, Integer.parseInt(items[3])));

                } else if (items[0].equals("Flight")) {
                    int i = cityIds.get(items[1]);
                    int j = cityIds.get(items[2]);
                    this.targets.add(new Path.Edge(i, j, 0));
                }
            }

            System.out.println();

            // build airlines
            this.airports = new int[cityIdx][cityIdx];

            for (Path.Edge e : airs)
            {
                airports[e.from][e.from] = 0;
                airports[e.to][e.to] = 0;
                airports[e.from][e.to] = e.cost + cities.get(e.from);
                airports[e.to][e.from] = e.cost + cities.get(e.to);
            }

            this.delta = cities.get(0);

            // update the cost of target
            for (Path.Edge e : this.targets)
            {
                e.cost = airports[e.from][e.to];
            }


        } catch (FileNotFoundException e) {
        } finally {
            if (sc != null) sc.close();
        }

        // init the planner according to the input
        this.planner = new AStarPlanner(this.getAirports(), this.getTargets(), delta);
    }

    public Path airelinePlan() {
//        this.planner = new AStarPlanner(this.getAirports(), this.getTargets(), delta);
        // generate the init set in A* algorithm
        planner.generateOpenSet();

        // check whether find the target
        while (!planner.isFinished()) {
            planner.generateOpenSet();
            planner.applyNextStep();
        }

        Path path = planner.getPath();

        path.setCityNames(this.cityNames);

        return path;
    }

    public int[][] getAirports() {
        return airports;
    }

    public void setAirports(int[][] airports) {
        this.airports = airports;
    }

    public List<Path.Edge> getTargets() {
        return targets;
    }

    public void setTargets(List<Path.Edge> targets) {
        this.targets = targets;
    }

    public void reset() {
        this.targets = null;
        this.airports = null;
    }
}
