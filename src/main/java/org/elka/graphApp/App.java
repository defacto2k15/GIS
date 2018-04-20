package org.elka.graphApp;

import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run(){
        System.out.println("Hello World!");

        GnpRandomGraphGenerator<Integer, MyWeightedEdge> generator = new GnpRandomGraphGenerator<>(6, 0.6, 123);
        WeightedGraph<Integer, MyWeightedEdge> generatedGraph = new SimpleDirectedWeightedGraph<>((e1, e2) -> new MyWeightedEdge());
        final int[] lastInt = {0};
        generator.generateGraph(generatedGraph, () -> (lastInt[0]++), null);

        Random random = new Random();
        for (MyWeightedEdge edge : generatedGraph.edgeSet()) {
            generatedGraph.setEdgeWeight(edge, random.nextDouble());
        }
        RemoveBidirectionalEdges(generatedGraph);

        DrawGraph(generatedGraph);
    }

    private <T1,T2 extends MyWeightedEdge> void RemoveBidirectionalEdges(WeightedGraph<T1,T2> graph) {
        HashMap<T1, List<T1>> neighbourVertices = new HashMap<>();
        List<T2> edgesToRemove = new ArrayList<>();

        for(T2 edge : graph.edgeSet()){
            T1 source = (T1)edge.getSource();
            T1 target = (T1)edge.getTarget();

            if( !neighbourVertices.containsKey(source)){
                neighbourVertices.put(source, new ArrayList<T1>());
            }
            if( !neighbourVertices.containsKey(target)){
                neighbourVertices.put(target, new ArrayList<T1>());
            }

            if(neighbourVertices.get(target).contains(source)){
                edgesToRemove.add(edge);
            }else {
                neighbourVertices.get(source).add(target);
            }
        }
        edgesToRemove.forEach(graph::removeEdge);
    }

    private <T1, T2> void DrawGraph(Graph<T1, T2> graph) {
        JGraphXAdapter<T1, T2> adapter = new JGraphXAdapter<>(graph);
        SimpleGraphWindow.DrawGraph(adapter);
    }

    public class MyWeightedEdge extends DefaultWeightedEdge {
        private DecimalFormat formater = new DecimalFormat("#.0####");

        @Override
        public String toString() {
            return  formater.format(getWeight());
        }

        @Override
        public Object getSource() {
            return super.getSource();
        }

        @Override
        protected Object getTarget() {
            return super.getTarget();
        }
    }
}
