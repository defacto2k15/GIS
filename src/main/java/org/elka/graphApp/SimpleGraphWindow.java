package org.elka.graphApp;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;

public class SimpleGraphWindow extends JApplet{
    private mxGraph graph;

    public SimpleGraphWindow(mxGraph graph) {
        this.graph = graph;
    }

    public static void DrawGraph(mxGraph graph) {
        SimpleGraphWindow window = new SimpleGraphWindow(graph);
        window.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(window);
        frame.setTitle("JGraphT Adapter to JGraphX Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    @Override
    public void init()
    {
        // create a visualization using JGraph, via an adapter
        setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent component = new mxGraphComponent(graph);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        getContentPane().add(component);
        resize(DEFAULT_SIZE);

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(graph);

        // center the circle
        int radius = 100;
        layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
        layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
        layout.setRadius(radius);
        layout.setMoveCircle(true);

        layout.execute(graph.getDefaultParent());
        // that's all there is to it!...
    }
}
