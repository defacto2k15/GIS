package org.elka.graphApp;

import com.google.devtools.common.options.OptionsParser;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.elka.graphApp.benchmark.Benchmark;
import org.elka.graphApp.benchmark.BenchmarkConfiguration;
import org.elka.graphApp.display.SimpleGraphWindow;
import org.elka.graphApp.runner.SimpleRunner;
import org.jgrapht.io.ImportException;

import java.io.IOException;
import java.util.Collections;


public class App {

    public static void main(String[] args) throws ImportException, CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        OptionsParser parser = OptionsParser.newOptionsParser(BenchmarkConfiguration.class);
        parser.parseAndExitUponError(args);
        BenchmarkConfiguration options = parser.getOptions(BenchmarkConfiguration.class);
        if (options == null || options.help) {
            printUsage(parser);
            return;
        }

        if(options.mode.toLowerCase().equals("test")){
            Benchmark.runBenchmark(options);
        }else if(options.mode.toLowerCase().equals("find") ){
            SimpleRunner.runOnUserInput(options);
        }else if(options.mode.toLowerCase().equals("generate") ) {
            GraphGeneration.runOnUserInput(options);
        }else{
            printUsage(parser);
        }
    }

    private static void printUsage(OptionsParser parser) {
        System.out.println("Usage: App -g MODE OPTIONS");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                OptionsParser.HelpVerbosity.LONG));
    }

    private static SimpleGraphWindow window;

}
