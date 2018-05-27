package org.elka.graphApp;

import com.google.devtools.common.options.OptionsParser;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.elka.graphApp.algorithms.SurballeBasedAlgorithmSolver;
import org.elka.graphApp.benchmark.Benchmark;
import org.elka.graphApp.benchmark.BenchmarkConfiguration;
import org.elka.graphApp.benchmark.BenchmarkExecutor;
import org.elka.graphApp.benchmark.SingleMeasure;
import org.elka.graphApp.display.LayoutMode;
import org.elka.graphApp.display.SimpleGraphWindow;
import org.elka.graphApp.generators.MySimpleGraphGenerator;
import org.elka.graphApp.runner.SimpleRunner;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.io.*;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;


public class App {

    public static void main(String[] args) throws ImportException, CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        OptionsParser parser = OptionsParser.newOptionsParser(BenchmarkConfiguration.class);
        parser.parseAndExitUponError(args);
        BenchmarkConfiguration options = parser.getOptions(BenchmarkConfiguration.class);
        if (options == null || options.isHelp()) {
            printUsage(parser);
            return;
        }
        Benchmark.runBenchmark(options);

        if (!"".equals(options.getInputFile())) {
            SimpleRunner.runOnUserInput(options);
        }
    }

    private static void printUsage(OptionsParser parser) {
        System.out.println("Usage: App OPTIONS");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                OptionsParser.HelpVerbosity.LONG));
    }

    private static SimpleGraphWindow window;


}
