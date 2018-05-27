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
            defaultValue = "true",
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
            name = "tests",
            abbrev = 'h',
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
//>>>>>>> 27bbd5f1c290c804316cbdacc82faf5f2a5f9b20

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
            converter = CommaSeparatedIntegers.class
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

//<<<<<<< HEAD
//    public List<Float> getWattsLerpKParamsPerTest() {
//        return Arrays.asList(0.2f, 0.4f, 0.6f, 0.8f);
//    }
//
//=======
    public boolean shouldTestSurballe() {
        return surballeTests;
    }


//>>>>>>> 27bbd5f1c290c804316cbdacc82faf5f2a5f9b20
}
