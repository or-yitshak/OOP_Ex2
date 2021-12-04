import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyDirectedWeightedGraph implements DirectedWeightedGraph {
    public HashMap<Integer, NodeData> key_node;
    public HashMap<int[], EdgeData> keys_edge;
    private int mc;

    public MyDirectedWeightedGraph() {
        this.key_node = new HashMap<>();
        this.keys_edge = new HashMap<>();
    }

    public MyDirectedWeightedGraph(DirectedWeightedGraph graph) // copy constructor
    {
        this.key_node = new HashMap<Integer, NodeData>();
        this.keys_edge = new HashMap<int[], EdgeData>();
        graph.edgeIter()
        graph.nodeIter()
        this.mc = graph.getMC();
    }

    @Override
    public NodeData getNode(int key) {
        return key_node.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        int[] edge_keys = {src, dest};
        return keys_edge.get(edge_keys);
    }

    @Override
    public void addNode(NodeData n) {
        int new_key = n.getKey();
        Node new_node = (Node) n;
        key_node.put(new_key, new_node);
    }

    @Override
    public void connect(int src, int dest, double w) {
        int[] new_edge_keys = {src, dest};
        Edge new_edge = new Edge(src, dest, w);
        keys_edge.put(new_edge_keys, new_edge);//add the new edge to the edges HashMap.
        Node dest_node = (Node) key_node.get(dest);
        dest_node.parents_ids.add(src);//add the src_id to the parents_ids HashSet of dest_node.
        Node src_node = (Node) key_node.get(src);
        src_node.children_ids.add(dest);//add the dest_id to the children_ids HashSet of src_node.
        src_node.edges_to_children.add(new_edge);//add the Edge to the Edge Hash set of src_node.
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        Collection<NodeData> nodes = key_node.values();
        return nodes.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        Collection<EdgeData> edges = keys_edge.values();
        return edges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        Node nd = (Node) key_node.get(node_id);
        HashSet<EdgeData> edges = nd.edges_to_children;
        return edges.iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        Node curr_node = (Node) key_node.get(key);
        for (int parent_id : curr_node.parents_ids) {
            int[] edge_keys = {parent_id, key};
            keys_edge.remove(edge_keys);
            Node parent_node = (Node) key_node.get(key);
            parent_node.children_ids.remove(key);
        }
        curr_node.parents_ids.clear();
        for (int child_id : curr_node.children_ids) {
            int[] edge_keys = {key, child_id};
            keys_edge.remove(edge_keys);
            Node child_node = (Node) key_node.get(key);
            child_node.parents_ids.remove(key);
        }
        curr_node.children_ids.clear();
        curr_node.edges_to_children.clear();
        key_node.remove(key);
        return curr_node;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        int[] edge_keys = {src, dest};
        Edge edge = (Edge) keys_edge.get(edge_keys);
        Node src_node = (Node) key_node.get(src);
        src_node.children_ids.remove(dest);
        src_node.edges_to_children.remove(edge);
        Node dest_node = (Node) key_node.get(dest);
        dest_node.parents_ids.remove(src);
        keys_edge.remove(edge_keys);
        return edge;
    }

    @Override
    public int nodeSize() {
        return key_node.size();
    }

    @Override
    public int edgeSize() {
        return keys_edge.size();
    }

    @Override
    public int getMC() {
        return mc;
    }
}
