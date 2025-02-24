/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.List;
import java.util.Random;

//TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot scelta
 * randomicamente tra le disponibili. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSortRandom<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    private static final Random randomGenerator = new Random();

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
    	if (l == null) {
            throw new NullPointerException();
        }
        quickSort(l, 0, l.size() - 1);
        return new SortingAlgorithmResult<>(l, 0);
    }

    @Override
    public String getName() {
        return "QuickSortRandom";
    }
    private void quickSort(List<E> l, int low, int high) {
        if (low < high) {
            int pivotIndex = randomPartition(l, low, high);
            quickSort(l, low, pivotIndex - 1); // Recursively sort the left part
            quickSort(l, pivotIndex + 1, high); // Recursively sort the right part
        }
    }

    private int randomPartition(List<E> l, int low, int high) {
        int pivotIndex = randomGenerator.nextInt(high - low + 1) + low;
        swap(l, pivotIndex, high); // Move pivot to end for partitioning
        return partition(l, low, high);
    }

    private int partition(List<E> l, int low, int high) {
        E pivot = l.get(high); // pivot
        int i = (low - 1); // Index of smaller element

        for (int j = low; j < high; j++) {
            // If the current element is smaller than or equal to pivot
            if (l.get(j).compareTo(pivot) <= 0) {
                i++;

                // swap arr[i] and arr[j]
                swap(l, i, j);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        swap(l, i + 1, high);

        return i + 1;
    }

    private void swap(List<E> l, int i, int j) {
        E temp = l.get(i);
        l.set(i, l.get(j));
        l.set(j, temp);
    }

}
