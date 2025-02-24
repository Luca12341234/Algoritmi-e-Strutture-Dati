/**
 * 
 */
package it.unicam.cs.asdl2425.es10;

import java.util.ArrayList;
import java.util.List;

// TODO completare import 

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare - Nota: usare una variante dei metodi della classe
        // MaxHeap in modo da implementare l'algoritmo utilizzando solo un array
        // (arraylist) e alcune variabili locali di appoggio (implementazione
        // cosiddetta "in loco" o "in place", si veda
        // https://it.wikipedia.org/wiki/Algoritmo_in_loco)
    	if (l == null) {
            throw new NullPointerException("La lista da ordinare non può essere nulla.");
        }

        // Copia della lista in un ArrayList per accesso efficiente tramite indici
        List<E> array = new ArrayList<>(l);
        int n = array.size();

        // Costruzione del Max Heap
        buildMaxHeap(array, n);

        // Variabile per tracciare le operazioni (opzionale)
        int operations = 0;

        // Estrazione degli elementi dal heap uno alla volta
        for (int i = n - 1; i > 0; i--) {
            // Scambio del massimo (radice) con l'elemento corrente
            swap(array, 0, i);
            operations += 3; // Supponiamo 1 operazione per lo scambio e 2 per gli accessi

            // Ripristino della proprietà del heap per la porzione rimanente
            heapify(array, i, 0);
        }

        // Aggiornamento della lista originale con gli elementi ordinati
        for (int i = 0; i < n; i++) {
            l.set(i, array.get(i));
        }

        // Creazione del risultato dell'ordinamento
        SortingAlgorithmResult<E> result = new SortingAlgorithmResult<>(l, operations);
        return result;
    }

    @Override
    public String getName() {
        return "HeapSort";
    }
    
    private void buildMaxHeap(List<E> array, int n) {
        // L'ultimo nodo non foglia è all'indice n/2 - 1
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }
    }
    
    private void heapify(List<E> array, int n, int index) {
        int largest = index; // Inizializzazione del più grande come radice
        int left = leftIndex(index); // Figlio sinistro
        int right = rightIndex(index); // Figlio destro

        // Se il figlio sinistro è più grande della radice
        if (left < n && array.get(left).compareTo(array.get(largest)) > 0) {
            largest = left;
        }

        // Se il figlio destro è più grande del più grande finora
        if (right < n && array.get(right).compareTo(array.get(largest)) > 0) {
            largest = right;
        }

        // Se il più grande non è la radice
        if (largest != index) {
            swap(array, index, largest);
            // Ricorsivamente heapify il sottoalbero interessato
            heapify(array, n, largest);
        }
    }
    
    private void swap(List<E> array, int i, int j) {
        E temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }
    
    private int rightIndex(int i) {
        return 2 * i + 2;
    }
    
    private int leftIndex(int i) {
        return 2 * i + 1;
    }

}
