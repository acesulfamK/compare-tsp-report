import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AntColonyOptimization {
    static int n;
    static float[][] dist;
    static int k = 100;
    static float[][] tau;
    static float[][] eta;
    static final float PHEROMONE_EVAPORATION_RATE = 0.5f;
    static final float ALPHA = 1;
    static final float BETA = 5;
    static final int ITERATIONS = 100;

    static class Ant {
        int current;
        ArrayList<Integer> omega = new ArrayList<>();

        public Ant(int startPos) {
            current = startPos;
            for (int i = 0; i < n; i++) {
                if (i != startPos) {
                    omega.add(i);
                }
            }
        }

        public float traverse() {
            float pathLength = 0;
            int start = current;
            ArrayList<Integer> visited = new ArrayList<>();
            visited.add(start);

            while (!omega.isEmpty()) {
                int next = selectNext();
                visited.add(next);
                pathLength += dist[current][next];
                omega.remove((Integer) next);
                current = next;
            }

            // Return to starting point
            pathLength += dist[current][start];
            updatePheromone(visited, pathLength);
            return pathLength;
        }

        public int selectNext() {
            float total = 0;
            for (int i : omega) {
                total += Math.pow(tau[current][i], ALPHA) * Math.pow(eta[current][i], BETA);
            }

            float selectedValue = new Random().nextFloat() * total;
            float currentSum = 0;
            for (int i : omega) {
                currentSum += Math.pow(tau[current][i], ALPHA) * Math.pow(eta[current][i], BETA);
                if (currentSum >= selectedValue) {
                    return i;
                }
            }
            return omega.get(0); 
        }

        public void updatePheromone(ArrayList<Integer> visited, float pathLength) {
            for (int i = 0; i < visited.size() - 1; i++) {
                int from = visited.get(i);
                int to = visited.get(i + 1);
                tau[from][to] = (1 - PHEROMONE_EVAPORATION_RATE) * tau[from][to] + (1 / pathLength);
            }
        }
    }

    public static float[] optimize() {
        initializeEta();
        initializeTau();
        float bestLength = Float.MAX_VALUE;

        for (int iteration = 0; iteration < ITERATIONS; iteration++) {
            for (int i = 0; i < k; i++) {
                Ant ant = new Ant(i % n); // Start each ant from a different node
                float currentLength = ant.traverse();
                if (currentLength < bestLength) {
                    bestLength = currentLength;
                }
            }
        }
        return new float[]{bestLength};
    }

    public static void initializeEta() {
        eta = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    eta[i][j] = 1 / dist[i][j];
                }
            }
        }
    }

    public static void initializeTau() {
        tau = new float[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(tau[i], 1.0f);
        }
    }

    public static void main(String[] args) {
        // Set up a sample graph
        n = 4;
        dist = new float[][] {
            {0, 10, 14, 10},
            {10, 0, 10, 14},
            {14, 10, 0, 10},
            {10, 14, 10, 0}
        };
        float[] result = optimize();
        System.out.println("Shortest path found: " + result[0]);
    }
}
