/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.List;
import java.util.LinkedList;
// TODO completare import

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
    	if (l == null) {
            throw new NullPointerException();
        }else if (l.size() <= 1) {
            return new SortingAlgorithmResult<>(l, 0);
        }
        
        int mid = l.size() / 2;
        List<E> leftHalf = l.subList(0, mid);
        List<E> rightHalf = l.subList(mid, l.size());

        SortingAlgorithmResult<E> sortedLeft = sort(leftHalf);
        SortingAlgorithmResult<E> sortedRight = sort(rightHalf);

        List<E> merged = merge(sortedLeft.getL(), sortedRight.getL());
        int totalComparisons = sortedLeft.getCountCompare() + sortedRight.getCountCompare();
        return new SortingAlgorithmResult<>(merged, totalComparisons);
    }

    public String getName() {
        return "MergeSort";
    }
    private List<E> merge(List<E> left, List<E> right) {
        int leftIndex = 0, rightIndex = 0;
        List<E> merged = new LinkedList<>();

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                merged.add(left.get(leftIndex++));
            } else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }
}
