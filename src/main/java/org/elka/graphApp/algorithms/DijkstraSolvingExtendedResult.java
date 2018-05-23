package org.elka.graphApp.algorithms;

import org.jgrapht.GraphPath;

import java.util.List;

/**
 * Created by defacto on 5/23/2018.
 */
public class DijkstraSolvingExtendedResult<V, E> {
    private List<GraphPath<V, E>> paths;
    private int triesCount;

    public DijkstraSolvingExtendedResult(List<GraphPath<V, E>> paths, int triesCount) {
        this.paths = paths;
        this.triesCount = triesCount;
    }

    public List<GraphPath<V, E>> getPaths() {
        return paths;
    }

    public int getTriesCount() {
        return triesCount;
    }
}
