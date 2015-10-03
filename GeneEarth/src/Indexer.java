/**
 * Indexer used to determine the indexes to use for filtering the result
 * of genes a gene is connected to. : sorts GR and R (Name is sorted by NameIndexer)
 */

import java.util.ArrayList;
import java.util.Comparator;

public class Indexer implements Comparator<Integer> {

    private final ArrayList<Double> list; // list that we will filter by

    // Constructor : passes in the arraylist which we are going to sort by
    // There are three possibilities : GR, R, and Name
    // Name will be sorted using NameIndexer
    public Indexer(ArrayList<Double> filterBy) {
        if (filterBy == null) {
            throw new NullPointerException("Invalid filter requested");
        } else {
            list = filterBy;
        }
    }

    //Create the indices
    public Integer[] createIndices() {
        Integer[] indices = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            indices[i] = i; // assign each index the corresponding number
        }
        return indices;
    }

    //Overridden compare method to sort the array
    @Override
    public int compare(Integer first, Integer second) {
        return list.get(second).compareTo(list.get(first));
    }

}

