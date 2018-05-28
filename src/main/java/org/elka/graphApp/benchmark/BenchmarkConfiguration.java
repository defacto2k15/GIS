package org.elka.graphApp.benchmark;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import lombok.Getter;
import lombok.Setter;
import org.elka.graphApp.parsing.CommaSeparatedFloats;
import org.elka.graphApp.parsing.CommaSeparatedIntegers;
import org.elka.graphApp.parsing.GraphReader;

import java.util.List;

/**
 * Created by defacto on 5/20/2018.
 */
@Getter
@Setter
public class BenchmarkConfiguration extends OptionsBase {

//<<<<<<< HEAD
//    public boolean shouldTestSurballe() {
//        return false;
//    }
//
//    public int getMinNodeCount() {
//        return 10;
//    }
//
//    public int getMaxNodeCount() {
//        return 200;
//    }
//
//    public int getTestsCount() {
//        return 50;
//    }
//
//    public int getPerGraphTestsCount(){
//        return 3;
//    }
//
//    public List<Float> getErdosPropabilitiesPerTest() {
//        return Arrays.asList(0.2f, 0.4f, 0.6f, 0.8f, 0.9f);
//    }
//=======
    @Option(
            name = "help",
            abbrev = 'h',
            help = "Prints usage info.",
            defaultValue = "false",
            category = "benchmark"
    )
    public boolean help;

    @Option(
            name = "minNodeCount",
            abbrev = 'n',
            help = "This sets minimal node count in tests",
            defaultValue = "10",
            category = "benchmark"
    )
    public int minNodeCount;
    @Option(
            name = "maxNodeCount",
            abbrev = 'm',
            help = "This sets maximal nodes count in tests",
            defaultValue = "100",
            category = "benchmark"
    )
    public int maxNodeCount;
    @Option(
            name = "dijkstra",
            abbrev = 'd',
            help = "This flag enables dijkstra tests",
            defaultValue = "true",
            category = "benchmark"
    )
    public boolean dijkstraTests;
    @Option(
            name = "surballe",
            abbrev = 's',
            help = "This flag enables surballe tests",
            defaultValue = "false",
            category = "benchmark"
    )
    public boolean surballeTests;
    @Option(
            name = "tests",
            abbrev = 't',
            help = "Sets number of tests",
            defaultValue = "60",
            category = "benchmark"
    )
    public int testsCount;

    @Option(
            name = "testsPerGraphCount",
            abbrev = 'q',
            help = "Sets number of tests per single graph",
            defaultValue = "40",
            category = "benchmark"
    )
    public int testsPerGraphCount;

    @Option(
            name = "erodosProbabilities",
            abbrev = 'e',
            help = "This sets erdos probabilities per test",
            allowMultiple = true,
            converter = CommaSeparatedFloats.class,
            defaultValue = "0.8f",
            category = "benchmark"
    )
    public List<Float> erdosProbabilitiesPerTest;
    @Option(
            name = "wattsProbabilities",
            abbrev = 'w',
            help = "This sets watts probabilities per test",
            converter = CommaSeparatedFloats.class,
            allowMultiple = true,
            category = "benchmark",
            defaultValue = "0.8f"
    )
    public List<Float> wattsProbabilitiesPerTest;

    @Option(
            name = "kWatts",
            abbrev = 'k',
            help = "This sets kWatts params per test",
            allowMultiple = true,
            defaultValue = "8",
            category = "benchmark",
            converter = CommaSeparatedIntegers.class
    )
    public List<Integer> wattsKParamsPerTest;

    @Option(
            name = "lerpkWatts",
            abbrev = 'l',
            help = "This sets kWatts per nodes lerp params per test",
            allowMultiple = true,
            defaultValue = "0.5",
            category = "benchmark",
            converter = CommaSeparatedFloats.class
    )
    public List<Float> wattsLerpKParamsPerTest;

    @Option(name = "testOutputFile",
            abbrev = 'o',
            help = "This option specifies the output file for test",
            defaultValue = "result.csv",
            category = "benchmark")
    public String outputFile;

    @Option(name = "inputFile",
            abbrev = 'i',
            help = "This options allows to test surballe and dijkstra algorithm on graph passed by user\n" +
            GraphReader.FILE_FORMAT,
            defaultValue = "",
            category = "user input" )
    public String inputFile;

    @Option(name = "dijkstraUser",
            abbrev = 'x',
            help = "Test for dijkstra",
            defaultValue = "true",
            category = "user input" )
    public boolean dijkstra;

    @Option(name = "surballeUser",
            abbrev = 'f',
            help = "Test for surballe",
            defaultValue = "true",
            category = "user input" )
    public boolean surballe;

    public boolean shouldTestDijkstra() {
        return dijkstraTests;
    }

    public boolean shouldTestSurballe() {
        return surballeTests;
    }

    @Option(name = "mode",
            abbrev = 'g',
            help = "mode of app",
            defaultValue = "test",
            category = "Mode setting" )
    public String mode;

    @Option(name = "startIndex",
            abbrev = 'z',
            help = "Index of start vertex in search",
            defaultValue = "1",
            category = "user input" )
    public  int startIndex;

    @Option(name = "endIndex",
            abbrev = 'c',
            help = "Index of end vertex in search",
            defaultValue = "1",
            category = "user input" )
    public int endIndex;

    @Option(name = "shouldShowWindow",
            abbrev = 'v',
            help = "Should window with paths be shown",
            defaultValue = "false",
            category = "user input" )
    public boolean shouldShowWindow;

    @Option(name = "generatorType",
            abbrev = 'u',
            help = "Grapg generator type",
            defaultValue = "erdos",
            category = "user input" )
    public String generatorType;

    @Option(name = "nodeCount",
            abbrev = 'y',
            help = "Nodes count in graph being generated",
            defaultValue = "5",
            category = "user input" )
    public int nodesCount;

    @Option(name = "seed",
            abbrev = 'j',
            help = "randomSeed",
            defaultValue = "5",
            category = "user input" )
    public int seed;
}
