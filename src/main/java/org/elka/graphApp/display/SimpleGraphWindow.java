package org.elka.graphApp.display;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SimpleGraphWindow extends JApplet {
    private mxGraph graph;
    private LayoutMode layoutMode;

    public SimpleGraphWindow(mxGraph graph, LayoutMode layoutMode) {
        this.graph = graph;
        this.layoutMode = layoutMode;
    }

    public SimpleGraphWindow(Graph graph) {
        this(new JGraphXAdapter<>(graph), LayoutMode.Circle);
    }

    public void DrawGraph() {
        init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setTitle("JGraphT Adapter to JGraphX Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static final Dimension DEFAULT_SIZE = new Dimension(800, 800);

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
        mxGraphLayout layout;
        Map<String, Object> vertexStyle = graph.getStylesheet().getDefaultVertexStyle();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        graph.getStylesheet().setDefaultVertexStyle(vertexStyle);

        if( layoutMode == LayoutMode.Organic) {
            mxOrganicLayout organicLayout = new mxOrganicLayout(graph, getBounds());

            organicLayout.setAverageNodeArea(40000);
            organicLayout.setInitialMoveRadius(300);
//        layout.setAverageNodeArea(10);
            organicLayout.setEdgeLengthCostFactor(0);
            organicLayout.setEdgeDistanceCostFactor(10000);
            organicLayout.setNodeDistributionCostFactor(10);
            layout = organicLayout;
        }else {
            mxCircleLayout circleLayout = new mxCircleLayout(graph);
            int radius = 100;
            circleLayout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
            circleLayout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
            circleLayout.setRadius(radius);
            circleLayout.setMoveCircle(true);
            layout = circleLayout;
        }

        layout.execute(graph.getDefaultParent());
        // that's all there is to it!...
    }

    public <V, E> void colorShortestPath(GraphPath<V, E> walk) {
        colorShortestPaths(Collections.singletonList(walk));
    }

    public <V, E> void colorShortestPaths(List<GraphPath<V, E>> walks) {
        if (walks.isEmpty()) {
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
