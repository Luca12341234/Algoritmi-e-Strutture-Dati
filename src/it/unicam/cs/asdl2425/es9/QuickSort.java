/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.List;

// TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
    	if (l == null) {
            throw new NullPointerException("List must not be null");
        }
        quickSort(l, 0, l.size() - 1);
        return new SortingAlgorithmResult<>(l, 0);
    }

    @Override
    public String getName() {
        return "QuickSort";
    }
    private void quickSort(List<E> l, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(l, low, high);
            quickSort(l, low, pivotIndex - 1); // Recursively sort the left part
            quickSort(l, pivotIndex + 1, high); // Recursively sort the right part
        }
    }

    private int partition(List<E> l, int low, int high) {
        E pivot = l.get(high); // Choose the last element as pivot
        int i = (low - 1); // Index of smaller element

        for (int j = low; j < high; j++) {
            // If the current element is smaller than the pivot
            if (l.get(j).compareTo(pivot) <= 0) {
                i++;
                // Swap l[i] and l[j]
                E temp = l.get(i);
                l.set(i, l.get(j));
                l.set(j, temp);
            }
        }

        // Swap l[i+1] and l[high] (or pivot)
        E temp = l.get(i + 1);
        l.set(i + 1, l.get(high));
        l.set(high, temp);

        return i + 1; // Return the partitioning index
    }

}
