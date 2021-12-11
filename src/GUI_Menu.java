import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUI_Menu extends JFrame implements ActionListener {
    public MyDirectedWeightedGraphAlgorithms graphAlgo = new MyDirectedWeightedGraphAlgorithms();
    JLabel label;
    GUI_Graph panel;
    JMenuBar menu_bar;

    public GUI_Menu() {
        panel = new GUI_Graph();
        label = new JLabel("Hello!");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.label.setHorizontalAlignment(JLabel.CENTER);

        this.add(label,BorderLayout.NORTH);
        this.add(panel);
        this.menu_bar = new JMenuBar();
        JMenu main_menu, load, run_alogs, edit;
        JMenuItem save, gson_g1, gson_g2, gson_g3, saved, show_graph;
        JMenuItem insert_node, insert_edge, remove_node, remove_edge;
        JMenuItem isConnected, shortestPathDist, shortestPath, center, tsp;

        main_menu = new JMenu("Menu");
        load = new JMenu("Load");
        run_alogs = new JMenu("Run Algorithms");
        save = new JMenuItem("Save");
        gson_g1 = new JMenuItem("G1");
        gson_g2 = new JMenuItem("G2");
        gson_g3 = new JMenuItem("G3");
        saved = new JMenuItem("Saved G");
        //show_graph = new JMenuItem("Show Graph");
        edit = new JMenu("Edit");
        insert_node = new JMenuItem("Insert Node");
        insert_edge = new JMenuItem("Insert Edge");
        remove_node = new JMenuItem("Remove Node");
        remove_edge = new JMenuItem("Remove Edge");
        isConnected = new JMenuItem("Is Connected");
        shortestPath = new JMenuItem("Shortest Path");
        shortestPathDist = new JMenuItem("Shortest Path Distance");
        center = new JMenuItem("Center");
        tsp = new JMenuItem("TSP");

        load.add(gson_g1);
        load.add(gson_g2);
        load.add(gson_g3);
        load.add(saved);

        run_alogs.add(isConnected);
        run_alogs.add(shortestPath);
        run_alogs.add(shortestPathDist);
        run_alogs.add(center);
        run_alogs.add(tsp);

        edit.add(insert_node);
        edit.add(insert_edge);
        edit.add(remove_node);
        edit.add(remove_edge);

        main_menu.add(save);
        main_menu.add(load);
        main_menu.add(edit);
        //main_menu.add(show_graph);
        main_menu.add(run_alogs);

        this.menu_bar.add(main_menu);
        this.setJMenuBar(this.menu_bar);

        save.addActionListener(this);
        gson_g1.addActionListener(this);
        gson_g2.addActionListener(this);
        gson_g3.addActionListener(this);
        saved.addActionListener(this);
        edit.addActionListener(this);
        //show_graph.addActionListener(this);
        isConnected.addActionListener(this);
        shortestPath.addActionListener(this);
        shortestPathDist.addActionListener(this);
        center.addActionListener(this);
        tsp.addActionListener(this);
        insert_edge.addActionListener(this);
        insert_node.addActionListener(this);
        remove_edge.addActionListener(this);
        remove_node.addActionListener(this);










        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }







    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand() + " selected");
        this.label.setText(e.getActionCommand() + " selected. please wait");
        switch (e.getActionCommand()) {
            case "G1" -> load_graph("G1");
            case "G2" -> load_graph("G2");
            case "G3" -> load_graph("G3");
            case "Saved G" -> load_graph("Saved G");
            case "Save" -> this.graphAlgo.save("output.json");
            case "Insert Node" -> fills_funcs("Insert Node");
            case "Insert Edge" -> fills_funcs("Insert Edge");
            case "Remove Node" -> fills_funcs("Remove Node");
            case "Remove Edge" -> fills_funcs("Remove Edge");
            case "Is Connected" -> answerBox("Is Connected");
            case "Shortest Path" -> fills_funcs("Shortest Path");
            case "Shortest Path Distance" -> fills_funcs("Shortest Path Distance");
            case "Center" -> answerBox("Center");
            case "TSP" -> fills_funcs("TSP");
        }

    }

    public boolean load_graph(String name) {
        String fullPath;
        if (name.startsWith("G"))
            fullPath = "data/" + name + ".json";
        else
            fullPath = "output.json";
        String message = "The Graph " + name + " has uploaded!";
        try {
            this.graphAlgo.load(fullPath);
            this.panel.graph_data = graphAlgo.getGraph();
            this.panel.updateUI();
            label.setText(message);
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            label.setText("Error: The Graph " + name + " couldn't be uploaded!");
            return false;
        }
    }

    public void fills_funcs(String functionName) {
//        gg.label.setText(functionName);
        TextBox tb = new TextBox(functionName, this.graphAlgo,this.panel);
    }

    public void answerBox(String func_name){

        JLabel messege = new JLabel("Please wait");
//            messege.setFont();
        messege.setHorizontalAlignment(JLabel.CENTER);
        JFrame frame = new JFrame();
        frame.setSize(400,200);
        frame.setLocationRelativeTo(null);
        frame.add(messege);
        frame.setVisible(true);
        if(this.graphAlgo.getGraph() == null){
            messege.setText("Please load a graph first");
            return;
        }

        switch (func_name) {
            case "Is Connected":
                boolean is_connected =  this.graphAlgo.isConnected();
                if(is_connected) {
                    messege.setText("True! this graph is connected");
                }
                else {
                    messege.setText("False! this graph is NOT connected");
                }
                break;
            case "Center":
                NodeData cen = this.graphAlgo.center();
                messege.setText("The key of the Center Node is: "+cen.getKey());
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    static class TextBox extends JFrame implements ActionListener {
        public MyDirectedWeightedGraphAlgorithms graphAlgo;
        public static String algorithm_name;
        public static String algorithm_input;
        public static JTextField textField;
        public static JPanel panel;
        public static JButton button;
        public static JLabel label;
        public static GUI_Graph main_panel;

        public TextBox(String functionName, MyDirectedWeightedGraphAlgorithms graphAlgo,GUI_Graph main_panel) {
            this.main_panel = main_panel;
            this.graphAlgo = graphAlgo;
            algorithm_name = functionName;
            panel = new JPanel();
            label = new JLabel("Please enter your input to " + functionName + ". \n");
            switch (functionName){
                case "Insert Node" -> label.setText("<html>"+label.getText() + "<br/>" +
                        "Enter the node name, X value and<br/> Y value, split them by ','" +
                        "<br/>If you like to edit node's <br/>location, enter it name.</html>");
                case "Remove Node" -> label.setText("<html>"+label.getText() + "<br/>" +
                        "Enter the name of the<br/> node which you like to remove.</html>");
                case "Insert Edge" -> label.setText("<html>"+label.getText() + "<br/>Select two node keys" +
                        " of the<br/> edge first for source, second for<br/> destination, then add the wanted weight" +
                        " <br/>split them by ','</html>");
                case "Remove Edge" -> label.setText("<html>"+label.getText() + "<br/>" +
                        "Select two node keys of the<br/> edge first for source, second for" +
                        "destination,<br/> split them by ','</html>");
                case "Shortest Path" -> label.setText("<html>"+label.getText() + "<br/>" +
                        "Select two node keys,<br/> split them by ','</html>");
                case "Shortest Path Distance" -> label.setText("<html>"+label.getText() + "<br/>" +
                        "Select two node keys, <br/>split them by ','</html>");
                case "TSP" -> label.setText("<html>"+label.getText() + "<br/>" +
                        "Select the wanted nodes,<br/> split them by ','</html>");
            }
            button = new JButton("Send");
            button.addActionListener(this);
            textField = new JTextField(16);
            JPanel panel = new JPanel();
            this.setContentPane(panel);
            panel.add(textField);
            panel.add(button);
            panel.add(label);
            this.setSize(300, 300);
            panel.show();
            this.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Send")) {
                // set the text of the label to the text of the field
                algorithm_input = textField.getText();
                textField.setText("");
                String input = this.algorithm_input;
                System.out.println(input);
                String[] in = input.split(",");
                System.out.println(Arrays.toString(in));

                JLabel messege = new JLabel("Please wait");
//            messege.setFont();
                messege.setHorizontalAlignment(JLabel.CENTER);
                JFrame frame = new JFrame();
                frame.setSize(400,200);
                frame.setLocationRelativeTo(null);
                frame.add(messege);
                frame.setVisible(true);
                if(this.graphAlgo.getGraph() == null){
                    messege.setText("Please load a graph first");
                    return;
                }

                switch (algorithm_name) {
                    case "Insert Node":
                        try {
                            if(in.length==3) {
                                int key_node = Integer.parseInt(in[0]); // if we have a node with this key???
                                double x = Double.parseDouble(in[1]);
                                double y = Double.parseDouble(in[2]);
                                MyGeoLocation location = new MyGeoLocation(x, y, 0);
                                Node newNode = new Node(key_node, location);
                                if (this.graphAlgo.getGraph().getNode(key_node) == null) {
                                    this.graphAlgo.getGraph().addNode(newNode); // new node
//                                    main_panel.updateUI();
                                    messege.setText("the Node has been added successfully");
                                } else {// change exist
                                    this.graphAlgo.getGraph().getNode(key_node).setLocation(location);
                                    messege.setText("OOPS! a Node with this key is already exists you should pick another key");
                                }
                            }
                            else {
                                messege.setText("that's wrong input");
                            }
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                    case "Insert Edge":
                        try {
                            if(in.length == 3) {
                                int src_new = Integer.parseInt(in[0]);
                                int dest_new = Integer.parseInt(in[1]);
                                double weight_new = Double.parseDouble(in[2]);
                                this.graphAlgo.getGraph().connect(src_new, dest_new, weight_new);
//                                main_panel.updateUI();
                                messege.setText("the Edge has been added successfully");
                            }
                            else{
                                messege.setText("that's wrong input");
                            }
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                    case "Remove Node":
                        try {
                            if (in.length == 1) {
                                int node_to_del = Integer.parseInt(in[0]);
                                if (this.graphAlgo.getGraph().getNode(node_to_del) != null) {
                                    this.graphAlgo.getGraph().removeNode(node_to_del);
//                                    main_panel.removeAll();
//                                    main_panel.updateUI();
                                    messege.setText("the Node has been removed successfully");
                                } else {
                                    messege.setText("there is no Node with this key");
                                }
                            }
                            else {
                                messege.setText("that's wrong input");
                            }
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                    case "Remove Edge":
                        try {
                            if(in.length==2) {
                                int src_del = Integer.parseInt(in[0]);
                                int dest_del = Integer.parseInt(in[1]);
                                if (this.graphAlgo.getGraph().getEdge(src_del, dest_del) != null) {
                                    this.graphAlgo.getGraph().removeEdge(src_del, dest_del);
//                                    main_panel.updateUI();
                                    messege.setText("the Edge has been removed successfully");
                                } else {
                                    messege.setText("this Edge dose not exist");
                                }
                            }
                            else{
                                messege.setText("that's wrong input");
                            }
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                    case "Shortest Path":
                        try {
                            int src = Integer.parseInt(in[0]);
                            int dest = Integer.parseInt(in[1]);
                            List<NodeData> ans = this.graphAlgo.shortestPath(src, dest);
                            String ans_str = "";
                            for (int i = 0; i < ans.size(); i++) {
                                if (i==ans.size()-1){
                                    ans_str+=ans.get(i).getKey();
                                }
                                else {
                                    ans_str+= ans.get(i).getKey()+"->";
                                }
                            }
                            messege.setText("the Shortest Path from "+src+" to "+dest+" is: "+ans_str);
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                    case "Shortest Path Distance":
                        try {
                            int src = Integer.parseInt(in[0]);
                            int dest = Integer.parseInt(in[1]);
                            double ans = this.graphAlgo.shortestPathDist(src, dest);
                            messege.setText("the Shortest Path Distance from "+src+" to "+dest+" is: "+ans);
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                    case "TSP":
                        try {
                            List<NodeData> cities = new ArrayList<>();
                            for (String str : in) {
                                int key = Integer.parseInt(str);
                                cities.add(graphAlgo.getGraph().getNode(key));
                            }
                            List<NodeData> ans = this.graphAlgo.tsp(cities);;
                            String ans_str = "";
                            for (int i = 0; i < ans.size(); i++) {
                                if (i==ans.size()-1){
                                    ans_str+=ans.get(i).getKey();
                                }
                                else {
                                    ans_str+= ans.get(i).getKey()+"->";
                                }
                            }
                            messege.setText("the TSP answer is: "+ans_str);
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                            messege.setText("ERROR, something went wrong (IllegalArgument)");
                        }
                        break;
                }
            }
        }
    }




    public static void main(String args[]){
        new GUI_Menu();
    }
}

