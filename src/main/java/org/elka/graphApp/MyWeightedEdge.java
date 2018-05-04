package org.elka.graphApp;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.text.DecimalFormat;

/**
 * Created by defacto on 5/2/2018.
 */
public class MyWeightedEdge<V> extends DefaultWeightedEdge {
    private DecimalFormat formater = new DecimalFormat("#.0####");

    @Override
    public String toString() {
        return formater.format(getWeight());
    }

    @Override
    public V getSource() {
        return (V) super.getSource();
    }

    @Override
    public V getTarget() {
        return (V) super.getTarget();
    }

}
