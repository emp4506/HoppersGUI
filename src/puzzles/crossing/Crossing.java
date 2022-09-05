package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;

/**
 * The main program to run the Crossings puzzle. It handles the command arguments as well as the
 * printing of the final data.
 *
 * @author Evan Prizel, emp4506
 */
public class Crossing {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
        } else {
            CrossingConfig startConfig = new CrossingConfig(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                    0, 0, 'L');
            CrossingConfig finalConfig = new CrossingConfig(0, 0,
                    Integer.parseInt(args[0]), Integer.parseInt(args[1]), 'R');
            Solver solver = new Solver();
            Collection<Configuration> path = solver.solve(startConfig, finalConfig);
            System.out.println("Pups: " + args[0] + ", Wolves: " + args[1]);
            System.out.println("Total Configs: " + solver.totalConfigs);
            System.out.println("Unique configs: " + solver.predecessors.size());
            if (!path.isEmpty()) {
                int i = 0;
                for (Configuration config : path) {
                    System.out.println("Step " + i + ": " + config);
                    ++i;
                }
            } else {
                System.out.println("No Solution");
            }
        }
    }
}
