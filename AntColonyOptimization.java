import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.io.IOException;

public class AntColonyOptimization {
    static int n;
    static float[][] dist;
    static int k = 100;
    static float[][] tau;
    static float[][] nextTau;
    static float[][] eta;
    static final float PHEROMONE_EVAPORATION_RATE = 0.5f;
    static final float ALPHA = 1;
    static final float BETA = 5;
    static final int ITERATIONS = 30;
    static boolean setVisible = false;
    static ArrayList<ArrayList<Integer>> outputPaths;
    static ArrayList<Float> xList;
    static ArrayList<Float> yList;

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
            outputPaths.add(visited);
            pathLength += dist[current][start];
            setPheromone(visited, pathLength);
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

        public void setPheromone(ArrayList<Integer> visited, float pathLength) {
            for (int i = 0; i < visited.size() - 1; i++) {
                int from = visited.get(i);
                int to = visited.get(i + 1);
                nextTau[from][to] += 1 / pathLength;
            }
        }
    }
    
    public static void updatePheromone(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                tau[i][j] = ( 1 - PHEROMONE_EVAPORATION_RATE) * tau[i][j] + nextTau[i][j];
            }
        }
    }

    static void distReset(){
        dist = new float[n][];
        for(int i=0;i<n;i++){
            dist[i] = new float[n];
        }
    }

    public static float[] optimize() {
        outputPaths = new ArrayList<>();
        initializeEta();
        initializeTau();
        float bestLength = Float.MAX_VALUE;

        for (int iteration = 0; iteration < ITERATIONS; iteration++) {
            initializeNextTau();
            for (int i = 0; i < k; i++) {
                Ant ant = new Ant(i % n); // Start each ant from a different node
                float currentLength = ant.traverse();
                if (currentLength < bestLength) {
                    bestLength = currentLength;
                }
            }
            updatePheromone();
        }
        return new float[]{bestLength};
    }

    public static void initializeNextTau() {
        nextTau = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    nextTau[i][j] = 0;
                }
            }
        }
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
    
    
    private static void loadGraphFromMatrix(){
        String filePath = "./graph/Oct.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String[] value1 = br.readLine().split(" ");
            n = Integer.parseInt(value1[0]);
            distReset();

            for(int i=0;i<n;i++){
                String[] strList = br.readLine().split(" ");
                for(int j=0;j<n;j++){
                    dist[i][j] = Float.parseFloat(strList[j]);
                }
            }
            
            //for(int i=0;i<n;i++){
            //    for(int j=0;j<n;j++){
            //        System.out.print(""+dist[i][j]+" ");
            //    }
            //    System.out.println();
            //}
            //

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    public static class Point {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double dist(Point p) {
            return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
        }
    }


    static void loadGraphFromPoint(String filepath){
        ArrayList<Point> vertices = new ArrayList<>();
        String filename = filepath;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String[] parts1 = br.readLine().split(" ");
            n = Integer.parseInt(parts1[0]);
            distReset();
            xList = new ArrayList<>();
            yList = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(" ");
                vertices.add(new Point(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
                xList.add(Float.parseFloat(parts[0]));
                yList.add(Float.parseFloat(parts[1]));
                
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        
        n = vertices.size();
        // テキストファイルに書き込み
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = (float) vertices.get(i).dist(vertices.get(j));
            }
        }
    }


    static void makeSceneFile(){
        // ファイルに書き込み
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./scene/ouput.txt"))) {
            writer.write(n+" ");
            writer.write(outputPaths.size()+" ");
            writer.newLine();
            for(int i=0;i<n;i++){
                writer.write(xList.get(i)+ " ");
                writer.write(yList.get(i)+ " ");
                writer.newLine();
            }
            for (ArrayList<Integer> row : outputPaths) {
                for (Integer num : row) {
                    writer.write(num + " ");
                }
                // 行の終わり
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    public static void main(String[] args) {
        // Set up a sample graph
        loadGraphFromPoint("./points/japan.txt");
        //loadGraphFromMatrix();
        float[] result = optimize();
        System.out.println("Shortest path found: " + result[0]);
        makeSceneFile();
    }
}
