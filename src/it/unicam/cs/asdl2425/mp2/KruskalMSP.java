package it.unicam.cs.asdl2425.mp2;

import java.util.Set;

//import necessari
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 * 
 * @param <L> tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMSP<L> {

	/*
	 * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
	 * dall'algoritmo di Kruskal.
	 */
	private ForestDisjointSets<GraphNode<L>> disjointSets;

	/**
	 * Costruisce un calcolatore di un albero di copertura minimo che usa
	 * l'algoritmo di Kruskal su un grafo non orientato e pesato.
	 */
	public KruskalMSP() {
		this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
	}

	/**
	 * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
	 * minimo in un grafo non orientato e pesato, con pesi degli archi non negativi.
	 * L'albero restituito non è radicato, quindi è rappresentato semplicemente con
	 * un sottoinsieme degli archi del grafo.
	 * 
	 * @param g un grafo non orientato, pesato, con pesi non negativi
	 * @return l'insieme degli archi del grafo g che costituiscono l'albero di
	 *         copertura minimo trovato
	 * @throw NullPointerException se il grafo g è null
	 * @throw IllegalArgumentException se il grafo g è orientato, non pesato o con
	 *        pesi negativi
	 */
	public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
		// controllo che il grafo sia null
		if (g == null)
			throw new NullPointerException("Il grafo è null");
		// controllo che il grafo sia orientato
		if (g.isDirected())
			throw new IllegalArgumentException("Il grafo è orientato");
		// scorro gli archi
		for (GraphEdge<L> archiCorrenti : g.getEdges()) {
			// se gli archi non hanno un peso oppure il loro peso è negativo allora richiamo
			// l'eccezione
			if (!archiCorrenti.hasWeight() || archiCorrenti.getWeight() < 0)
				throw new IllegalArgumentException("Arco senza peso o con peso negativo");
		}
		// inizializzo la foresta di nodi disgiunti
		this.disjointSets.clear();
		for (GraphNode<L> nodoCorrente : g.getNodes())
			this.disjointSets.makeSet(nodoCorrente);
		// ordino gli archi come vuole l'algoritmo di Kruskal
		// uso quicksort perchè è in loco e sopratutto ha quasi sempre O(n log n)
		List<GraphEdge<L>> listaArchi = new ArrayList<>(g.getEdges());
		this.quickSort(listaArchi, 0, listaArchi.size() - 1);
		// inizizzo il set
		Set<GraphEdge<L>> archiMSP = new HashSet<>();
		for (GraphEdge<L> arcoCorrente : listaArchi) {
			// prendo i nodi che sono collegati mediante quell'arco
			GraphNode<L> nodoUno = arcoCorrente.getNode1();
			GraphNode<L> nodoDue = arcoCorrente.getNode2();
			// controllo che i riferiemtni non siano uguali per fare l'unione
			if (!this.disjointSets.findSet(nodoUno).equals(this.disjointSets.findSet(nodoDue))) {
				this.disjointSets.union(nodoUno, nodoDue);
				// aggiungo l'arco al set
				archiMSP.add(arcoCorrente);
			}
		}
		return archiMSP;
	}

	// creo dei metodi private per fini di implementazione

	/**
	 * Ordina con l'algoritmo QuickSort. COMPLESSITA' OTTIMO = O(n log n) ; MEDIO =
	 * O(n log n) ; PESSIMO = O(n^2) ;
	 * 
	 * @param archi  lista di archi da ordinare
	 * @param inizio indice iniziale della lista da ordinare
	 * @param fine   indice finale della lista da ordinare
	 */
	private void quickSort(List<GraphEdge<L>> archi, int inizio, int fine) {
		// controllo che la lista degli archi non sia null

		if (inizio < fine) {
			// creo la partizione
			int indicePivot = partition(archi, inizio, fine);
			// richiamo quickSort in maniera ricorsiva
			quickSort(archi, inizio, indicePivot - 1);
			quickSort(archi, indicePivot + 1, fine);
		}
	}

	/**
	 * Partiziona una porzione della lista per implementare il QuickSort
	 * 
	 * @param archi  lista di archi da partizionare
	 * @param inizio indice iniziale
	 * @param fine   indice finale
	 * @return indice finale dopo la partizione
	 */
	private int partition(List<GraphEdge<L>> archi, int inizio, int fine) {
		// Uso l'elemento in posizione finale come pivot
		GraphEdge<L> pivot = archi.get(fine);
		double pesoPivot = pivot.getWeight();

		// Indice del più grande elemento minore del pivot
		int i = inizio - 1;

		for (int j = inizio; j < fine; j++) {
			// Se l'elemento corrente ha un peso minore o uguale al pivot...
			if (archi.get(j).getWeight() <= pesoPivot) {
				i++;
				// ...faccio lo swap
				swap(archi, i, j);
			}
		}
		// faccio lo swap
		swap(archi, i + 1, fine);
		return i + 1;
	}

	/**
	 * Scambia due elementi della lista archi
	 * 
	 * @param archi lista di archi per fare swap
	 * @param i     indice primo arco
	 * @param j     indice secondo arco
	 */
	private void swap(List<GraphEdge<L>> archi, int i, int j) {
		// uso una variabile di appoggio per fare lo swap
		GraphEdge<L> appoggio = archi.get(i);
		archi.set(i, archi.get(j));
		archi.set(j, appoggio);
	}

}
