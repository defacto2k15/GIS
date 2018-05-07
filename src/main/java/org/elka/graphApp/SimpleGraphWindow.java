package org.elka.graphApp;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import org.jgrapht.GraphPath;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SimpleGraphWindow extends JApplet {
    private mxGraph graph;

    public SimpleGraphWindow(mxGraph graph) {
        this.graph = graph;
    }

    public void DrawGraph() {
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
    public void init() {
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

    public <V, E> void colorShortestPath(GraphPath<V, E> walk) {
        colorShortestPaths(Collections.singletonList(walk));
    }

    public <V, E> void colorShortestPaths(List<GraphPath<V, E>> walks) {
        if (walks.isEmpty()) {
            System.out.println("No paths in argument!!!");
            return;
        }
        GraphPath<V, E> firstPath = walks.get(0);
        GraphPath<V, E> secondPath = null;
        if (walks.size() > 1) {
            secondPath = walks.get(1);
        }
        GraphPath<V, E> finalSecondPath = secondPath;

        Arrays.stream(graph.getChildCells(graph.getDefaultParent())).map(c -> (mxCell) c).forEach(cell -> {
            String color = null;
            if (cell.isVertex()) {
                if (cell.getValue().equals(firstPath.getStartVertex())) {
                    color = MyColorConstants.ShortestPathStartColor;
                } else if (cell.getValue().equals(firstPath.getEndVertex())) {
                    color = MyColorConstants.ShortestPathEndColor;
                } else if (firstPath.getVertexList().contains(cell.getValue())) {
                    color = MyColorConstants.ShortestPathNodeColor;
                } else if (finalSecondPath != null) {
                    if (finalSecondPath.getVertexList().contains(cell.getValue())) {
                        color = MyColorConstants.SecondShortestPathNodeColor;
                    }
                }
            } else if (cell.isEdge()) {
                if (firstPath.getEdgeList().contains(cell.getValue())) {
                    color = MyColorConstants.ShortestPathEdgeColor;
                } else if (finalSecondPath != null) {
                    if (finalSecondPath.getEdgeList().contains(cell.getValue())) {
                        color = MyColorConstants.SecondShortestPathEdgeColor;
                    }
                }
            }

            if (color != null) {
                Map<String, Object> styleMap = new HashMap<>(graph.getCellStyle(cell));
                if (cell.isVertex()) {
                    styleMap.put(mxConstants.STYLE_FILLCOLOR, color);
                } else if (cell.isEdge()) {
                    styleMap.put(mxConstants.STYLE_STROKECOLOR, color);
                }
                graph.getView().getState(cell).setStyle(styleMap);
            }
        });
    }
}
