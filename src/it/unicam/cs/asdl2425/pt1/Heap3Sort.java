package it.unicam.cs.asdl2425.pt1;

import java.util.List;

/**
 * Classe che implementa un algoritmo di ordinamento basato su uno heap ternario.
 * Usa una variante dei metodi di un MaxHeap ternario in modo da implementare l'algoritmo
 * utilizzando solo un array (arraylist) e alcune variabili locali di appoggio.
 * 
 * Uno heap ternario è uno heap in cui ogni nodo ha tre figli, anziché due.
 * 
 * Lo heap ternario accetta elementi ripetuti e non accetta elementi null.
 * 
 * @author Luca Tesei
 *         // TODO INSERIRE NOME, COGNOME ED EMAIL xxxx@studenti.unicam.it DELLO STUDENTE
 */
public class Heap3Sort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    // (Eventuali variabili istanza non sono strettamente necessarie per l'algoritmo in loco)

    /**
     * Esegue l'ordinamento in loco della lista passata usando un heap ternario.
     * 
     * La strategia è la seguente:
     * 1. Costruire il max-heap (heap ternario) in loco.
     * 2. Estrarre ripetutamente l'elemento massimo (situato in posizione 0) scambiandolo
     *    con l'ultimo elemento del sottoarray non ordinato, riducendo la dimensione dell'heap e
     *    richiamando heapify.
     * 
     * Durante il processo viene contato il numero di confronti effettuati.
     * 
     * @param l la lista di elementi da ordinare (non null)
     * @return un oggetto SortingAlgorithmResult contenente la lista ordinata e il numero di confronti effettuati
     */
    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if (l == null)
            throw new NullPointerException("La lista da ordinare non può essere null");
        
        int n = l.size();
        // Utilizziamo un contatore per i confronti (wrapper semplice)
        Counter counter = new Counter();

        // Costruisci il max-heap ternario (heapify partendo dagli indici "interni")
        // L'ultimo indice interno si trova a floor((n-1)/3)
        for (int i = (n - 1) / 3; i >= 0; i--) {
            heapify(l, i, n, counter);
        }

        // Procedura di Heapsort:
        // Ripetutamente, scambia l'elemento in posizione 0 (il massimo) con l'ultimo
        // elemento del sottoarray non ordinato e riduci la dimensione dell'heap.
        for (int end = n - 1; end > 0; end--) {
            swap(l, 0, end);
            heapify(l, 0, end, counter);
        }

        // Creiamo l'istanza del risultato usando il costruttore corretto
        return new SortingAlgorithmResult<>(l, (int) counter.count);
    }

    /**
     * Effettua l'heapify per un nodo con indice i in un heap di dimensione heapSize.
     * Adatta la procedura per un heap ternario.
     * 
     * @param l la lista che rappresenta il heap
     * @param i indice del nodo da heapificare
     * @param heapSize dimensione corrente dell'heap (gli elementi oltre heapSize sono ordinati)
     * @param counter contatore dei confronti (viene incrementato all'interno)
     */
    private void heapify(List<E> l, int i, int heapSize, Counter counter) {
        int largest = i;  // Presumiamo che il nodo corrente sia il più grande
        int fc = firstChild(i);
        int sc = secondChild(i);
        int tc = thirdChild(i);

        // Controlla il primo figlio, se esiste
        if (fc < heapSize) {
            counter.count++;
            if (l.get(fc).compareTo(l.get(largest)) > 0) {
                largest = fc;
            }
        }
        // Controlla il secondo figlio, se esiste
        if (sc < heapSize) {
            counter.count++;
            if (l.get(sc).compareTo(l.get(largest)) > 0) {
                largest = sc;
            }
        }
        // Controlla il terzo figlio, se esiste
        if (tc < heapSize) {
            counter.count++;
            if (l.get(tc).compareTo(l.get(largest)) > 0) {
                largest = tc;
            }
        }

        // Se uno dei figli è maggiore del nodo corrente, scambia e continua l'heapify
        if (largest != i) {
            swap(l, i, largest);
            heapify(l, largest, heapSize, counter);
        }
    }

    /**
     * Restituisce il nome dell'algoritmo di ordinamento.
     * 
     * @return "Heap3Sort"
     */
    @Override
    public String getName() {
        return "Heap3Sort";
    }

    /**
     * Restituisce l'indice del primo figlio per un nodo in posizione i.
     * 
     * @param i l'indice del nodo genitore
     * @return l'indice del primo figlio
     */
    private int firstChild(int i) {
        return 3 * i + 1;
    }

    /**
     * Restituisce l'indice del secondo figlio per un nodo in posizione i.
     * 
     * @param i l'indice del nodo genitore
     * @return l'indice del secondo figlio
     */
    private int secondChild(int i) {
        return 3 * i + 2;
    }

    /**
     * Restituisce l'indice del terzo figlio per un nodo in posizione i.
     * 
     * @param i l'indice del nodo genitore
     * @return l'indice del terzo figlio
     */
    private int thirdChild(int i) {
        return 3 * i + 3;
    }

    /**
     * Scambia due elementi della lista alle posizioni i e j.
     * 
     * @param l la lista in cui effettuare lo scambio
     * @param i indice del primo elemento
     * @param j indice del secondo elemento
     */
    private void swap(List<E> l, int i, int j) {
        E temp = l.get(i);
        l.set(i, l.get(j));
        l.set(j, temp);
    }

    /**
     * Classe helper per contare il numero di confronti effettuati.
     */
    private class Counter {
        long count = 0;
    }
}
