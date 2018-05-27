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
            defaultValue = "600",
            category = "benchmark"
    )
    public int testsCount;


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


}
