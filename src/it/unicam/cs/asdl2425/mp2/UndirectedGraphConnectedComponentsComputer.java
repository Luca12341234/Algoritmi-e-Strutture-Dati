package it.unicam.cs.asdl2425.mp2;

import java.util.Set;

//import necessari
import java.util.HashSet;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe singoletto che realizza un calcolatore delle componenti connesse di un
 * grafo non orientato utilizzando una struttura dati efficiente (fornita dalla
 * classe {@ForestDisjointSets<GraphNode<L>>}) per gestire insiemi disgiunti di
 * nodi del grafo che sono, alla fine del calcolo, le componenti connesse.
 * 
 * @author Luca Tesei (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 *
 * @param <L> il tipo delle etichette dei nodi del grafo
 */
public class UndirectedGraphConnectedComponentsComputer<L> {

	/*
	 * Struttura dati per gli insiemi disgiunti.
	 */
	private ForestDisjointSets<GraphNode<L>> f;

	/**
	 * Crea un calcolatore di componenti connesse.
	 */
	public UndirectedGraphConnectedComponentsComputer() {
		this.f = new ForestDisjointSets<GraphNode<L>>();
	}

	/**
	 * Calcola le componenti connesse di un grafo non orientato utilizzando una
	 * collezione di insiemi disgiunti.
	 * 
	 * @param g un grafo non orientato
	 * @return un insieme di componenti connesse, ognuna rappresentata da un insieme
	 *         di nodi del grafo
	 * @throws NullPointerException     se il grafo passato è nullo
	 * @throws IllegalArgumentException se il grafo passato è orientato
	 */
	public Set<Set<GraphNode<L>>> computeConnectedComponents(Graph<L> g) {
		// controllo che il grafo sia null
		if (g == null)
			throw new NullPointerException("Il grafo è null");
		// controllo che il grafo sia orientato
		if (g.isDirected())
			throw new IllegalArgumentException("Il grafo è orientato");
		// controllo che il grafo sia vuoto
		if (g.isEmpty())
			return new HashSet<>();
		// inizializzo la foresta di nodi disgiunti
		this.f.clear();
		for (GraphNode<L> nodoCorrente : g.getNodes())
			this.f.makeSet(nodoCorrente);
		// faccio l'unione dei nodi di ogni arco
		for (GraphEdge<L> insiemeComponenti : g.getEdges())
			this.f.union(insiemeComponenti.getNode1(), insiemeComponenti.getNode2());
		// creo il set
		Set<Set<GraphNode<L>>> risultato = new HashSet<>();
		// In ogni componente connessa aggiungo l'insieme dei suoi nodi al risultato
		for (GraphNode<L> rappresentante : this.f.getCurrentRepresentatives())
			risultato.add(this.f.getCurrentElementsOfSetContaining(rappresentante));
		// restituisco il set
		return risultato;

	}
}
