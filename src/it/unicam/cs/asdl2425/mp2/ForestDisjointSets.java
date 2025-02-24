package it.unicam.cs.asdl2425.mp2;

import java.util.Map;
import java.util.Set;

//import necessari
import java.util.HashSet;
import java.util.HashMap;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 *
 * @param <E> il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

	/*
	 * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
	 * albero della foresta. La variabile è protected unicamente per permettere i
	 * test JUnit.
	 */
	protected Map<E, Node<E>> currentElements;

	/*
	 * Classe interna statica che rappresenta i nodi degli alberi della foresta. Gli
	 * specificatori sono tutti protected unicamente per permettere i test JUnit.
	 */
	protected static class Node<E> {
		/*
		 * L'elemento associato a questo nodo
		 */
		protected E item;

		/*
		 * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui il nodo
		 * sia la radice allora questo puntatore punta al nodo stesso.
		 */
		protected Node<E> parent;

		/*
		 * Il rango del nodo definito come limite superiore all'altezza del
		 * (sotto)albero di cui questo nodo è radice.
		 */
		protected int rank;

		/**
		 * Costruisce un nodo radice con parent che punta a se stesso e rango zero.
		 * 
		 * @param item l'elemento conservato in questo nodo
		 * 
		 */
		public Node(E item) {
			this.item = item;
			this.parent = this;
			this.rank = 0;
		}

	}

	/**
	 * Costruisce una foresta vuota di insiemi disgiunti rappresentati da alberi.
	 */
	public ForestDisjointSets() {
		// iniziliazzo una nuova mappa
		this.currentElements = new HashMap<>();
	}

	@Override
	public boolean isPresent(E e) {
		// controllo che l'elemento non sia null
		if (e == null)
			throw new NullPointerException("L'elemento è null");
		// controllo che esso è una chiave della mappa
		return this.currentElements.containsKey(e);
	}

	/*
	 * Crea un albero della foresta consistente di un solo nodo di rango zero il cui
	 * parent è se stesso.
	 */
	@Override
	public void makeSet(E e) {
		// controllo che l'elemento è presente
		if (this.isPresent(e))
			throw new IllegalArgumentException("L'elemento si trova nella mappa");
		// se non lo è allora posso metterlo dentro
		this.currentElements.put(e, new Node<>(e));
	}

	/*
	 * L'implementazione del find-set deve realizzare l'euristica
	 * "compressione del cammino". Si vedano le istruzioni o il libro di testo
	 * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
	 */
	@Override
	public E findSet(E e) {
		// controllo che l'elemento non sia presente
		if (!this.isPresent(e))
			throw new IllegalArgumentException("L'elemento non si trova nella mappa");
		// applico il findSet in una delle sue tante versioni
		Node<E> nodoCorrente = this.currentElements.get(e);
		while (nodoCorrente.parent != nodoCorrente) {
			nodoCorrente.parent = nodoCorrente.parent.parent;
			nodoCorrente = nodoCorrente.parent;
		}
		return nodoCorrente.item;
	}

	/*
	 * L'implementazione dell'unione deve realizzare l'euristica "unione per rango".
	 * Si vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
	 * Capitolo 21 Sezione 3. In particolare, il rappresentante dell'unione dovrà
	 * essere il rappresentante dell'insieme il cui corrispondente albero ha radice
	 * con rango più alto. Nel caso in cui il rango della radice dell'albero di cui
	 * fa parte e1 sia uguale al rango della radice dell'albero di cui fa parte e2
	 * il rappresentante dell'unione sarà il rappresentante dell'insieme di cui fa
	 * parte e2.
	 */
	@Override
	public void union(E e1, E e2) {
		// prendo i riferimenti degli elementi
		E rappresentante1 = this.findSet(e1);
		E rappresentante2 = this.findSet(e2);
		// controllo che non siano uguali e se lo sono esco dal metodo
		if (rappresentante1.equals(rappresentante2))
			return;
		// prendo i loro nodi
		Node<E> radiceUno = this.currentElements.get(rappresentante1);
		Node<E> radiceDue = this.currentElements.get(rappresentante2);
		// controllo i rank per fare l'unione
		this.link(radiceUno, radiceDue);

	}

	@Override
	public Set<E> getCurrentRepresentatives() {
		// inizilizzo il set dove mettere i rappresentati
		Set<E> rappresentantiCorrenti = new HashSet<>();
		// prendo i rappresentati dei nodi
		for (Node<E> nodoCorrente : this.currentElements.values())
			rappresentantiCorrenti.add(this.findSet(nodoCorrente.item));
		// restituisco il set
		return rappresentantiCorrenti;
	}

	@Override
	public Set<E> getCurrentElementsOfSetContaining(E e) {
		// prendo il raprpesentante dell'elemento
		E rappresentante = this.findSet(e);
		// inizilizzo il set
		Set<E> elementiCollegati = new HashSet<>();
		// scorro i valori della mappa
		for (Node<E> nodoCorrente : this.currentElements.values()) {
			// controllo se il rappresentate del parametro è uguale agli altri
			// rappresentanti
			if (this.findSet(nodoCorrente.item).equals(rappresentante))
				// se lo è metto l'item nel set
				elementiCollegati.add(nodoCorrente.item);
		}
		// resttiuisco il set
		return elementiCollegati;
	}

	@Override
	public void clear() {
		// richiamo la funzione clear
		this.currentElements.clear();
	}

	// creo dei metodi private per fini di implementazione

	/**
	 * Esegue l'unione di due alberi nella foresta basandosi sull'euristica
	 * dell'unione per rango.
	 *
	 * @param nodoUno la radice del primo albero
	 * @param nodoDue la radice del secondo albero
	 */
	private void link(Node<E> nodoUno, Node<E> nodoDue) {
		// controllo nodoUno.rank > nodoDue.rank
		if (nodoUno.rank > nodoDue.rank) {
			nodoDue.parent = nodoUno;
		} else {
			// caso in cui nodoUno.rank <= nodoDue.rank
			nodoUno.parent = nodoDue;
			nodoDue.rank += (nodoUno.rank == nodoDue.rank) ? 1 : 0;
		}
	}

}
