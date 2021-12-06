import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


import java.util.List;
import java.util.List;

public class MyDirectedWeightedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    private MyDirectedWeightedGraph graph;
    HashMap<Integer,HashMap<Integer,Double>> id_distances;

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = (MyDirectedWeightedGraph) g;
        this.id_distances = new HashMap<>();
        HashSet<Integer> keys = new HashSet<>(this.graph.getKey_node().keySet());
        for (int key : keys) {
            HashMap<Integer,Double> distances = DijkstraAlgorithm(key);
            id_distances.put(key,distances);
        }
    }


    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new MyDirectedWeightedGraph(this.graph);
    }

    @Override
    public boolean isConnected() {
        if (this.graph.nodeSize() > this.graph.edgeSize() + 1)
            return false; // in connected graph we have at least n-1 edges for n vertices
        Node n = (Node) this.graph.getNode(1); // ???
        return this.graph.BFS_search(n) == this.graph.nodeSize();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<Integer, Double> distances = new HashMap();
        ArrayList<Integer> not_visited = new ArrayList<>();
        HashSet<Integer> keys = new HashSet<>(this.graph.getKey_node().keySet());
        for (int key : keys) {
            distances.put(key, Double.MAX_VALUE);
            not_visited.add(key);
        }
        distances.put(src, (double) 0);
        while (!not_visited.isEmpty()) {
            int min_id = minDistance(not_visited, distances);
            if(min_id == -1){
                break;
            }
            Node curr_node = (Node) this.graph.getNode(min_id);
            not_visited.remove((Object) min_id);
            for (EdgeData edge : curr_node.edges_to_children) {
                Edge curr_edge = (Edge) edge;
                int curr_dest = curr_edge.getDest();
                if (!not_visited.contains(curr_dest)) {//necessary?
                    continue;
                }
                double curr_weight = distances.get(curr_dest);
                double new_weight = distances.get(min_id) + curr_edge.getWeight();
                if (new_weight < curr_weight) {
                    distances.put(curr_dest, new_weight);
                }
            }
        }
        double shortest = distances.get(dest);
        if (shortest == Double.MAX_VALUE) {
            return -1;
        } else {
            return shortest;
        }
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        LinkedList<NodeData> path = new LinkedList<>();
        HashMap<Integer, Integer> previous = new HashMap();
        HashMap<Integer, Double> distances = new HashMap();
        ArrayList<Integer> not_visited = new ArrayList<>();
        HashSet<Integer> keys = new HashSet<>(this.graph.getKey_node().keySet());
        for (int key : keys) {
            distances.put(key, Double.MAX_VALUE);
            not_visited.add(key);
        }
        distances.put(src, (double) 0);
        while (!not_visited.isEmpty()) {
            int curr_src = minDistance(not_visited, distances);
            if(curr_src == -1){
                break;
            }
            Node curr_node = (Node) this.graph.getNode(curr_src);
            not_visited.remove((Object) curr_src);
            for (EdgeData edge : curr_node.edges_to_children) {
                Edge curr_edge = (Edge) edge;
                int curr_dest = curr_edge.getDest();
                if (!not_visited.contains(curr_dest)) {//necessary?
                    continue;
                }
                double curr_weight = distances.get(curr_dest);
                double new_weight = distances.get(curr_src) + curr_edge.getWeight();
                if (new_weight < curr_weight) {
                    distances.put(curr_dest, new_weight);
                    previous.put(curr_dest, curr_src);
                }
            }
        }
        if(distances.get(dest)==Double.MAX_VALUE){
            return null;
        }
        int curr = dest;
        while (curr != src){
            NodeData curr_node = this.graph.getNode(curr);
            path.addFirst(curr_node);
            curr = previous.get(curr);
        }
        NodeData src_node = this.graph.getNode(src);
        path.addFirst(src_node);
        return path;
    }

    @Override
    public NodeData center() {

        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        ArrayList<List<NodeData>> all_paths = getAllPermutation(cities);
        int min_index = -1;
        double min_cost = Double.MAX_VALUE;
        for (int i = 0; i < all_paths.size(); i++) {
            List<NodeData> curr_path = all_paths.get(i);
            double curr_cost = calcCost(curr_path);
            if(curr_cost<min_cost){
                min_cost = curr_cost;
                min_index = i;
            }
        }
        List<NodeData> shortest_path = all_paths.get(min_index);
        return shortest_path;
    }

    private double calcCost(List<NodeData> curr_path) {
        double cost = 0;
        for (int i = 0; i < curr_path.size()-1; i++) {
            NodeData src = curr_path.get(i);
            NodeData dest = curr_path.get(i+1);
            cost += id_distances.get(src.getKey()).get(dest.getKey());
        }
        return cost;
    }

    private static ArrayList<List<NodeData>> getAllPermutation(List<NodeData> cities){
        ArrayList<List<NodeData>> all_paths = new ArrayList<>();
        List<NodeData> curr_path = new ArrayList<>(cities);
        getAllPermutationRec(cities,curr_path,0,all_paths);
        return all_paths;
    }

    private static void getAllPermutationRec(List<NodeData> cities,List<NodeData> curr_path,int counter,ArrayList<List<NodeData>> all_paths){
        if(counter == cities.size()){
            List<NodeData> path_copy = new ArrayList<>(curr_path);
            all_paths.add(path_copy);
        }
        else {
            for (int i = counter; i < cities.size(); i++) {
                swap(curr_path, i, counter);
                getAllPermutationRec(cities, curr_path, counter + 1, all_paths);
                swap(curr_path, i, counter);
            }
        }
    }

    private static void swap(List<NodeData> x,int i,int j){
        NodeData tempi = x.get(i);
        NodeData tempj = x.get(j);
        x.set(i,tempj);
        x.set(j,tempi);
    }

    @Override
    public boolean save(String file)
    {
        return false;
    }

    @Override
    public boolean load(String file) // the path of the JSON file
    {
        try {
            FileReader fileReader = new FileReader(file);
            GsonBuilder builder = new GsonBuilder();
            JsonDeserializer<MyDirectedWeightedGraph> deserializer = new JsonDeserializer<MyDirectedWeightedGraph>() {
                @Override
                public MyDirectedWeightedGraph deserialize(JsonElement json, Type typeOf, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject jsonObj = json.getAsJsonObject();
                    MyDirectedWeightedGraph graph = new MyDirectedWeightedGraph();
                    JsonArray jsonArrayNodes = jsonObj.get("Nodes").getAsJsonArray();
                    double w, x, y, z;
                    for (int i = 0; i < jsonArrayNodes.size(); i++)
                    {
                        JsonObject jsonObjectNode = jsonArrayNodes.get(i).getAsJsonObject();
                        int key = jsonObjectNode.get("id").getAsInt();
                        String posStr = jsonObjectNode.get("pos").getAsString();
                        String [] posParams = posStr.split(",");
                        x = Double.parseDouble(posParams[0]);
                        y = Double.parseDouble(posParams[1]);
                        z = Double.parseDouble(posParams[2]);
                        MyGeoLocation pos = new MyGeoLocation(x, y, z);
                        graph.addNode(new Node(key, pos));
                    }
                    JsonArray jsonArrayEdges = jsonObj.get("Edges").getAsJsonArray();
                    int src, dest;
                    for (int i = 0; i < jsonArrayEdges.size(); i++)
                    {
                        JsonObject jsonObjectEdge = jsonArrayEdges.get(i).getAsJsonObject();
                        src = jsonObjectEdge.get("src").getAsInt();
                        w = jsonObjectEdge.get("w").getAsDouble();
                        dest = jsonObjectEdge.get("dest").getAsInt();
                        graph.connect(src, dest, w);
                    }
                    return graph;
                }
            };
            builder.registerTypeAdapter(MyDirectedWeightedGraph.class, deserializer);
            Gson customGson = builder.create();
            MyDirectedWeightedGraph graph = customGson.fromJson(fileReader, MyDirectedWeightedGraph.class);
            System.out.println(graph);
            this.graph = graph;
            return true;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    private HashMap<Integer, Double> DijkstraAlgorithm(int src) {
        HashMap<Integer, Double> distances = new HashMap();
        ArrayList<Integer> not_visited = new ArrayList<>();
        HashSet<Integer> keys = new HashSet<>(this.graph.getKey_node().keySet());
        for (int key : keys) {
            distances.put(key, Double.MAX_VALUE);
            not_visited.add(key);
        }
        distances.put(src, (double) 0);
        while (!not_visited.isEmpty()) {
            int min_id = minDistance(not_visited, distances);
            if(min_id == -1){
                break;
            }
            Node curr_node = (Node) this.graph.getNode(min_id);
            not_visited.remove((Object) min_id);
            for (EdgeData edge : curr_node.edges_to_children) {
                Edge curr_edge = (Edge) edge;
                int curr_dest = curr_edge.getDest();
                if (!not_visited.contains(curr_dest)) {//necessary?
                    continue;
                }
                double curr_weight = distances.get(curr_dest);
                double new_weight = distances.get(min_id) + curr_edge.getWeight();
                if (new_weight < curr_weight) {
                    distances.put(curr_dest, new_weight);
                }
            }
        }
        return distances;
    }

    private int minDistance(ArrayList<Integer> not_visited, HashMap<Integer, Double> distances) {
        int ans_id = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < not_visited.size(); i++) {
            int curr_id = not_visited.get(i);
            double curr_distance = distances.get(curr_id);
            if (curr_distance < min) {
                min = curr_distance;
                ans_id = curr_id;
            }
        }
        return ans_id;
    }

}
