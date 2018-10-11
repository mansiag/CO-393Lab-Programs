
import java.util.ArrayList;
import java.util.Scanner;
class Edge{
    int v1;
    int v2;
    Edge(int v1, int v2){
        this.v1 = v1;
        this.v2 = v2;
    }
}

class Graph {
        int V;
        ArrayList<ArrayList<Integer>> addlist;
        ArrayList<ArrayList<Edge>> allcycles;
        boolean visited_dfs[];

        Graph(int v) {
            this.V = v;
            addlist =  new ArrayList<>(v);
            for(int i=0; i<v; i++){
                ArrayList<Integer>temp = new ArrayList<Integer>();
                addlist.add(temp);
            }
            visited_dfs = new boolean[v];
            for(int i=0;i<v;i++){
                visited_dfs[i] = false;
            }
            allcycles = new ArrayList<ArrayList<Integer>>();
        }

        void creategraph(int v1, int v2) {
            addlist.get(v1).add(v2);
        }

        void printgraph() {
            for(int i=0; i<this.V; i++){
                System.out.println("edges coming out of"+i+"th node");
                for(int j=0; j<addlist.get(i).size(); j++)
                    System.out.print(addlist.get(i).get(j));
                System.out.println();
            }
        }

        boolean check_visited(Object a, ArrayList<Integer>path) {
            int n = path.size();
            for(int i=1; i<n; i++){
                if(path.get(i) == a){
                    return true;
                }
            }
            return false;
        }

        void find_cycle(Integer start, Integer current, ArrayList<Integer>path) {
            ArrayList<Integer> copypath =  new ArrayList<Integer>();
            copypath.addAll(path);
            copypath.add(current);
            int curr_size = addlist.get(current).size();
            for(int i=0; i<curr_size; i++){
                if(!check_visited(addlist.get(current).get(i),copypath)){
                    if(addlist.get(current).get(i)==start){
                        copypath.add(start);
                        allcycles.add(copypath);
                        return;
                    }
                    else{
                        find_cycle(start,addlist.get(current).get(i),copypath);
                    }
                }
            }
        }

        void print_all_cycles() {
            int m = allcycles.size();
            System.out.println("Following are all cycles:");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < allcycles.get(i).size(); j++) {
                    System.out.print(allcycles.get(i).get(j) + " -> ");
                }
                System.out.println();
          }
        }

        void print_big_cycles(){
            remove_subset_cycles();
            int m = allcycles.size();
            System.out.println("Following are the cycles:");
            for(int i=0; i<m; i++) {
                for (int j = 0; j < allcycles.get(i).size(); j++) {
                    System.out.print(allcycles.get(i).get(j)+" -> ");
                }
                System.out.println();
            }
        }

        void find_all_cycles(){
            ArrayList<Integer>path = new ArrayList<Integer>();
            for(int k=0;k<this.V;k++){
//                int m = allcycles.size();
//                for(int i=0; i<m; i++) {
//                    for (int j = 0; j < allcycles.get(i).size(); j++) {
//                        visited_dfs[allcycles.get(i).get(j)] = true;
//                    }}
//                if(!visited_dfs[k]){
                find_cycle(k,k,path);
            }
        }

        void remove_subset_cycles() {
            for (int i = 0; i < allcycles.size() - 1; i++) {
                for (int j = i + 1; j < allcycles.size(); j++) {
                    if (allcycles.get(i).containsAll(allcycles.get(j))) {
                        allcycles.remove(j);
                        j--;
                    } else if (allcycles.get(j).containsAll(allcycles.get(i))) {
                        allcycles.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }

    }

    public class Main{
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter number of nodes");
            int v = sc.nextInt();
            System.out.println("Enter number of edges");
            int e = sc.nextInt();
            Graph g = new Graph(v);
            System.out.println("Enter edges with nodes: outnode, innode");
            for(int i=0; i<e; i++) {
                int v1 = sc.nextInt();
                int v2 = sc.nextInt();
                g.creategraph(v1, v2);
            }
            //g.printgraph();
            System.out.println("All cycles are:");
            g.find_all_cycles();
            g.print_all_cycles();
            g.print_big_cycles();

        }
    }
