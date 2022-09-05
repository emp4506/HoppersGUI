package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.Collection;

/**
 * This runs the solver on a given configuration to test if the hoppers config and solver work.
 *
 * @author Evan Prizel, emp4506
 */
public class Hoppers {

    /**
     * Actually runs the solver on the configuration.
     *
     * @param args The command arguments
     * @throws IOException Thrown if the input the user gives for the filename is invalid.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {
            HoppersConfig Hoppers = new HoppersConfig(args[0]);
            Solver solver = new Solver();
            Collection<Configuration> path = solver.solve(Hoppers, null);
            System.out.println(Hoppers);
            System.out.println("Total Configs: " + solver.totalConfigs);
            System.out.println("Unique configs: " + solver.predecessors.size());
            if (!path.isEmpty()) {
                int i = 0;
                for (Configuration config : path) {
                    System.out.println("Step " + i + ":");
                    System.out.println(config);
                    ++i;
                }
            } else {
                System.out.println("No Solution");
            }
        }
    }
}
