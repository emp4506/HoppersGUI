package puzzles.common.solver;

import java.util.*;

/**
 * Solves the puzzle that is provided, using BFS
 *
 * @author Evan Prizel, emp4506
 */
public class Solver {

    /**
     * The current configuration the BFS is on.
     */
    private Configuration currConfig;

    /**
     * The total number of configurations created.
     */
    public int totalConfigs;

    /**
     * The Hashmap that contains the predecessors list.
     */
    public Map<Configuration, Configuration> predecessors;


    /**
     * Initializes the solver with a fresh number of totalConfigs.
     */
    public Solver() {
        totalConfigs = 0;
    }

    /**
     * BFS SOLVER
     * Creates the predecessor map and sends it off to the method that creates the actual path.
     *
     * @param config      The initial configuration we start with.
     * @param finalConfig The final configuration we are supposed to have.
     * @return the path from the initial config to the final config using BFS
     */
    public Collection<Configuration> solve(Configuration config, Configuration finalConfig) {
        List<Configuration> queue = new LinkedList<>();
        queue.add(config);
        totalConfigs++;
        predecessors = new HashMap<>();
        predecessors.put(config, null);
        while (!queue.isEmpty()) {
            currConfig = queue.remove(0);
            if (currConfig.isSolution()) {
                finalConfig = currConfig;
                break;
            }
            for (Configuration cne : currConfig.getNeighbors()) {
                if (!predecessors.containsKey(cne)) {
                    predecessors.put(cne, currConfig);
                    queue.add(cne);
                }
                totalConfigs++;
            }
        }
        return createPath(predecessors, config, finalConfig);
    }

    /**
     * Creates the actual path using the predecessor map provided by the previous function.
     *
     * @param predecessors The predecessor map provided by the previous function.
     * @param startConfig  The starting configuration.
     * @param finalConfig  The final configuration.
     * @return The path created.
     */
    private List<Configuration> createPath(Map<Configuration, Configuration> predecessors, Configuration startConfig, Configuration finalConfig) {
        List<Configuration> path = new LinkedList<>();
        if (predecessors.containsKey(finalConfig)) {
            Configuration currConfig = finalConfig;
            while (currConfig != startConfig) {
                path.add(0, currConfig);
                currConfig = predecessors.get(currConfig);
            }
            path.add(0, startConfig);
        }
        return path;
    }
}
