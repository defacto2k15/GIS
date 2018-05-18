package org.elka.graphApp.algorithms;

import java.util.List;

public class ForbiddenNodesTreeVertex<V>{
    private List<ForbiddenNodesTreeVertex> children;
    private List<V> forbiddenNodes;

    public ForbiddenNodesTreeVertex(List<V> forbiddenNodes) {
        this.forbiddenNodes = forbiddenNodes;
    }

    public void setChildren(List<ForbiddenNodesTreeVertex> children) {
        this.children = children;
    }

    public List<V> getForbiddenNodes() {
        return forbiddenNodes;
    }
}
