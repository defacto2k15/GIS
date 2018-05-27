package org.elka.graphApp.algorithms;

import org.jgrapht.GraphPath;

import java.util.*;
import java.util.stream.Collectors;

public class ForbiddenNodesRegister<V,E>{
    ForbiddenNodesTreeVertex<V> activeNode;
    Queue<ForbiddenNodesTreeVertex<V>> travesalQueue = new ArrayDeque<>();

    public ForbiddenNodesRegister(GraphPath<V, E> path) {
        travesalQueue.addAll(
        path.getVertexList().stream()
                    .skip(1).limit(path.getVertexList().size() - 2)
                    .map(c -> new ForbiddenNodesTreeVertex<V>(Collections.singletonList(c)))
                    .collect(Collectors.toList()));
    }

    public List<V> GetNextForbiddenNodes() {
        if(travesalQueue.isEmpty()){
            return null;
        }else {
            activeNode = travesalQueue.peek();
            return travesalQueue.remove().getForbiddenNodes();
        }
    }

    public void AddNewTraversedPath(GraphPath<V,E> path) {
        travesalQueue.addAll(
                path.getVertexList().stream()
                        .skip(1).limit(path.getVertexList().size() - 2)
                        .map((V c) -> {
                            List<V> list = new ArrayList<V>();
                            list.add(c);

                            list.addAll(activeNode.getForbiddenNodes());
                            return new ForbiddenNodesTreeVertex<>(list);
                        })
                        .collect(Collectors.toList()));
    }
}
