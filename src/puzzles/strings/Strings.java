package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;

/**
 * The main program to run the Strings puzzle. It handles the command arguments as well as the
 * printing of the final data.
 *
 * @author Evan Prizel, emp4506
 */
public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            StringsConfig stringSolver = new StringsConfig(args[0], args[1]);
            StringsConfig finalString = new StringsConfig(args[1], args[1]);
            double start = System.currentTimeMillis();
            Solver solver = new Solver();
            Collection<Configuration> path = solver.solve(stringSolver, finalString);
            double elapsed = (System.currentTimeMillis() - start) / 1000.0;
            System.out.println("Start: " + stringSolver + ", End: " + finalString);
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
            System.out.println("\nElapsed time: " + elapsed + " seconds.");
        }
    }
}
