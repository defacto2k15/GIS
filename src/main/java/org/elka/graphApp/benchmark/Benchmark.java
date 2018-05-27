package org.elka.graphApp.benchmark;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jgrapht.io.ImportException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class Benchmark {

    public static void runBenchmark(BenchmarkConfiguration options) throws ImportException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        BenchmarkExecutor executor = new BenchmarkExecutor();
        List<SingleMeasure> measures = executor.Execute(options);
        saveMeasuresToFile(measures, options.getOutputFile());
        return;
    }

    private static void saveMeasuresToFile(List<SingleMeasure> measures, String fileName) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = new FileWriter(fileName);
        StatefulBeanToCsv<SingleMeasure> beanToCsv = new StatefulBeanToCsvBuilder<SingleMeasure>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
        beanToCsv.write(measures);
        writer.close();
    }
}
