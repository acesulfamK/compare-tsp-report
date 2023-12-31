import java.util.*;
import java.io.*;

public class BranchBound{
    static float[][] dist;
    static int n;
    static ArrayList<Integer> minPath;
    static ArrayList<Integer> currentPath;
    static float pruningValue;
    static boolean setVisible = false;
    static ArrayList<ArrayList<Integer>> outputPath;
    static ArrayList<Float> xList;
    static ArrayList<Float> yList;
    static int counter = 0;

    
    static void distReset(){
        dist = new float[n][];
        for(int i=0;i<n;i++){
            dist[i] = new float[n];
        }
    }
    
    static boolean isPruning(){
        return lengthOfPath(currentPath)>pruningValue;
    }
    
    static float lengthOfPath(ArrayList<Integer> path){
        float sum=0;
        for(int i=0;i<path.size()-1;i++){
            sum += dist[path.get(i)][path.get(i+1)];
        }
        return sum;
    }
    
    static float lengthOfCycle(ArrayList<Integer> path){
        return lengthOfPath(path)+dist[path.get(path.size()-1)][path.get(0)];
    }
    
    static void branchBoundFunc(int node){
        if(counter>1000)
            return;
        currentPath.add(node);
        if(setVisible){
            System.out.println("now: "+currentPath.toString());
        }

        if(isPruning()){
            if(setVisible)
                System.out.println("Prune!");
            currentPath.remove(currentPath.size()-1);
            return; 
        }
        if(currentPath.size() == n){
            counter++;
            System.out.println("Cycle found!! : "+currentPath.toString());
            System.out.println("length: "+lengthOfCycle(currentPath));
            outputPath.add(new ArrayList<>(currentPath));
            if(minPath.size() == 0 || lengthOfCycle(minPath)>lengthOfCycle(currentPath)){
                minPath = new ArrayList<>(currentPath);
            }
        }
        for(int i=0;i<n;i++){
            if(!currentPath.contains(i)){
                branchBoundFunc(i);
            }
        }
        currentPath.remove(currentPath.size()-1);
        return;
    }

    static void branchBound(){
        currentPath = new ArrayList<>();
        minPath = new ArrayList<>();
        outputPath = new ArrayList<>();
        counter = 0;
        branchBoundFunc(0);
    }
    

    private static void loadGraphFromMatrix(){
        String filePath = "./graph/Oct.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String[] value1 = br.readLine().split(" ");
            n = Integer.parseInt(value1[0]);
            pruningValue = Integer.parseInt(value1[1]);
            distReset();
            for(int i=0;i<n;i++){
                String[] strList = br.readLine().split(" ");
                for(int j=0;j<n;j++){
                    dist[i][j] = Float.parseFloat(strList[j]);
                }
            }
            
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    System.out.print(""+dist[i][j]+" ");
                }
                System.out.println();
            }
            

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
            pruningValue = Float.parseFloat(parts1[1]);
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
            writer.write(outputPath.size()+" ");
            writer.newLine();
            for(int i=0;i<n;i++){
                writer.write(xList.get(i)+ " ");
                writer.write(yList.get(i)+ " ");
                writer.newLine();
            }
            for (ArrayList<Integer> row : outputPath) {
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

    public static void main(String[] args){
        loadGraphFromPoint("./points/japan.txt");
        //loadGraphFromMatrix();
        branchBound();
        System.out.println("min path :"+minPath.toString());
        System.out.println("min length : "+lengthOfCycle(minPath));
        makeSceneFile();
    }
}