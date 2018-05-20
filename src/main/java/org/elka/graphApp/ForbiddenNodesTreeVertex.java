package org.elka.graphApp;

import java.util.List;

public class ForbiddenNodesTreeVertex<V>{
    private List<V> forbiddenNodes;

    public ForbiddenNodesTreeVertex(List<V> forbiddenNodes) {
        this.forbiddenNodes = forbiddenNodes;
    }
    public List<V> getForbiddenNodes() {
        return forbiddenNodes;
    }
}
