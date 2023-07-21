import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BranchBound{
    static float[][] dist;
    static int n;
    static boolean[] visited;
    static int[] minPath;
    static ArrayList<Integer> currentPath;
    static float pruningValue;


    static void visitedReset(){
        visited = new boolean[n];
        for(int i=0;i<n;i++){
            visited[i] = false;
        }
    }
    
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
        currentPath.add(node);
        System.out.println("now: "+currentPath.toString());

        if(isPruning()){
            System.out.println("Prune!");
            currentPath.remove(currentPath.size()-1);
            return;            
        }
        if(currentPath.size() == n){
            System.out.println("Cycle found!! : "+currentPath.toString());
            System.out.println("length: "+lengthOfCycle(currentPath));
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
        visitedReset();
        currentPath = new ArrayList<>();
        branchBoundFunc(0);
    }
    

    private static void makeGraph(){
        String filePath = "./graph/square.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String[] value1 = br.readLine().split(" ");
            n = Integer.parseInt(value1[0]);
            pruningValue = Integer.parseInt(value1[1]);
            int counter = 0;
            String line;
            distReset();
            while((line = br.readLine()) != null){
                if(counter/n == counter%n){
                    dist[counter/n][counter%n] = 0;
                    counter++;
                }
                dist[counter/n][counter%n] = Float.parseFloat(line);
                counter++;
            }
            
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    System.out.print(""+dist[i][j]+" ");
                }
                System.out.println();
            }
            
            branchBound();

        } catch (IOException e){
            e.printStackTrace();
        }
        
        
    }
    
    public static void main(String[] args){
        makeGraph();
    }
}