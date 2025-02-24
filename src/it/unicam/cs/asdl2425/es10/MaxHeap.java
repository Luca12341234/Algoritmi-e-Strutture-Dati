package it.unicam.cs.asdl2425.es10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        // TODO implementare
    	if (list == null) {
            throw new NullPointerException("La lista fornita è nulla.");
        }
        for (E el : list) {
            if (el == null) {
                throw new NullPointerException("La lista contiene elementi nulli.");
            }
        }
        this.heap = new ArrayList<>(list);
        buildMaxHeap();
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        // TODO implementare
    	if (el == null) {
            throw new NullPointerException("L'elemento da inserire non può essere null.");
        }
        heap.add(el);
        siftUp(heap.size() - 1);
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        // TODO implementare
    	return 2 * i + 1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        // TODO implementare
    	return 2 * i + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        // TODO implementare
    	if (i == 0) {
            throw new IllegalArgumentException("La radice non ha un genitore.");
        }
        return (i - 1) / 2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        // TODO implementare
        if (isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        // TODO implementare
    	if (isEmpty()) {
            return null;
        }
        E max = heap.get(0);
        E last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapify(0);
        }
        return max;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        // TODO implementare
    	int n = heap.size();
        int largest = i;
        int left = leftIndex(i);
        int right = rightIndex(i);

        // Verifica se il figlio sinistro esiste e è maggiore del nodo corrente
        if (left < n && heap.get(left).compareTo(heap.get(largest)) > 0) {
            largest = left;
        }

        // Verifica se il figlio destro esiste e è maggiore del nodo più grande finora
        if (right < n && heap.get(right).compareTo(heap.get(largest)) > 0) {
            largest = right;
        }

        // Se il più grande non è il nodo corrente, scambia e ricorsivamente heapify
        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
    
    private void buildMaxHeap() {
        int n = heap.size();
        // L'ultimo nodo non foglia è all'indice n/2 - 1
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(i);
        }
    }
    
    private void siftUp(int i) {
        while (i > 0) {
            int parent = parentIndex(i);
            if (heap.get(i).compareTo(heap.get(parent)) > 0) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }
    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
