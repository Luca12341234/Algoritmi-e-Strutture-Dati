package it.unicam.cs.asdl2425.es9;

import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
    	int comparino = 0;

        for (int i = 1; i < l.size(); i++) {
            E key = l.get(i);
            int j = i - 1;
            while (j >= 0 && l.get(j).compareTo(key) > 0) {
                l.set(j + 1, l.get(j));
                j--;
                comparino++;
            }
            l.set(j + 1, key);
        }
        return new SortingAlgorithmResult<>(l, comparino);
    }

    public String getName() {
        return "InsertionSort";
    }
}
