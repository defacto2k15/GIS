package org.elka.graphApp;

import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.elka.graphApp.algorithms.SurballeBasedAlgorithmSolver;
import org.elka.graphApp.benchmark.BenchmarkConfiguration;
import org.elka.graphApp.display.SimpleGraphWindow;
import org.elka.graphApp.generators.MyErdosRenyiGraphGenerator;
import org.elka.graphApp.generators.MyWattsStrogatzGraphGenerator;
import org.elka.graphApp.parsing.GraphReader;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by defacto on 5/28/2018.
 */
public class GraphGeneration {
    public static void runOnUserInput(BenchmarkConfiguration options) {
        Graph<Integer, MyWeightedEdge<Integer>> graph;

        if(options.generatorType.toLowerCase().equals("erdos")){
            MyErdosRenyiGraphGenerator generator = new MyErdosRenyiGraphGenerator();
            graph = generator.Generate(options.nodesCount, options.erdosProbabilitiesPerTest.get(0), options.seed);
        } else
        if(options.generatorType.toLowerCase().equals("watts")){
            MyWattsStrogatzGraphGenerator generator = new MyWattsStrogatzGraphGenerator();
            graph = generator.Generate(options.nodesCount, options.wattsKParamsPerTest.get(0), options
                    .wattsProbabilitiesPerTest.get(0), options .seed );
        }else{
            System.err.println("Not recognized generatorType "+options.generatorType);
            return;
        }

        GraphReader.writeGraphToCsv(graph, options.outputFile);
    }

    private static void writeGraphPathsInfo(List<GraphPath<Integer, MyWeightedEdge<Integer>>> graphPaths) {
        System.out.println("Wyniki poszukiwania:");
        System.out.println("Znaleziono ścieżek: "+graphPaths.size());
        for(int i = 0; i < graphPaths.size(); i++){
            System.out.println("Ściezka "+i+": koszt: "+graphPaths.get(i).getWeight());
            System.out.print("Poszczególne segmenty ścieżki: ");
            for( Integer v : graphPaths.get(i).getVertexList()){
                System.out.print(v+" - ");
            }
            System.out.println();
        }
    }
}
