/**
 * String Indexer used to determine the indexes to use for filtering the result
 * of genes a gene is connected to.
 *
 */

import java.util.Comparator;


public class NameIndexer implements Comparator<Integer> {

    private final String[] list; // list that we will filter by

    // Constructor : passes in the arraylist which we are going to sort by
    // There are three possibilities : GR, R, and Name
    public NameIndexer(String[] filterBy){
        if(filterBy == null){
            throw new NullPointerException("Invalid filter requested");
        }else{
            list = filterBy;
        }
    }
    //Create the indices
    public Integer[] createIndices(){
        Integer[] indices = new Integer[list.length];
        for(int i=0; i<list.length;i++){
            indices[i]=i; // assign each index the corresponding number
        }
        return indices;
    }

    //Overridden compare method to sort the array
    @Override
    public int compare(Integer first, Integer second){
        return list[first].compareTo(list[second]);
    }

}

