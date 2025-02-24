/**
 * 
 */
package it.unicam.cs.asdl2425.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import necessari
import java.util.HashSet;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei, Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
	/*
	 * Le seguenti variabili istanza sono protected al solo scopo di agevolare il
	 * JUnit testing
	 */

	// Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
	// matrice di adiacenza
	protected Map<GraphNode<L>, Integer> nodesIndex;

	// Matrice di adiacenza, gli elementi sono null o oggetti della classe
	// GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
	// dimensione gradualmente ad ogni inserimento di un nuovo nodo.
	protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

	/**
	 * Crea un grafo vuoto.
	 */
	public AdjacencyMatrixUndirectedGraph() {
		this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
		this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
	}

	@Override
	public int nodeCount() {
		// i nodi sono uguali alla dimensione della matrice
		return this.matrix.size();
	}

	@Override
	public int edgeCount() {
		// prendo il set di edge di getEdges e poi prendo la sua dimensione
		return this.getEdges().size();
	}

	@Override
	public void clear() {
		// faccio il clear
		this.nodesIndex.clear();
		this.matrix.clear();
	}

	@Override
	public boolean isDirected() {
		// la classe implementa un grafo non orientato
		return false;
	}

	@Override
	public Set<GraphNode<L>> getNodes() {
		// creo una nuova copia del keySet
		return new HashSet<>(this.nodesIndex.keySet());
	}

	@Override
	public boolean addNode(GraphNode<L> node) {
		// controllo che il nodo sia già contenuto
		if (this.containsNode(node))
			return false;
		// prendo il numero di nodi e poi aggiungo il nodo alla mappa
		int posizioneNuova = this.nodeCount();
		this.nodesIndex.put(node, posizioneNuova);
		// creo una nuova riga
		ArrayList<GraphEdge<L>> listaAggiuntiva = new ArrayList<>(posizioneNuova + 1);
		for (int i = 0; i < posizioneNuova; i++) {
			// aggiungo un elemento alla nuova riga
			listaAggiuntiva.add(null);
			// aggiungo un elemento ad ogni riga per la colonna
			this.matrix.get(i).add(null);
		}
		// aggiungo l'ultima cella della nuova colonna
		listaAggiuntiva.add(null);
		// aggiungo la nuova riga
		this.matrix.add(listaAggiuntiva);
		return true;

	}

	@Override
	public boolean removeNode(GraphNode<L> node) {
		// controllo che il nodo non sia già contenuto
		if (!this.containsNode(node))
			return false;
		// prendo l'indice del nodo
		int indiceCancellazione = this.nodesIndex.get(node);
		// rimuovo il nodo dalla mappa e dalla riga
		this.nodesIndex.remove(node);
		this.matrix.remove(indiceCancellazione);
		// rimuovo la colonna
		for (ArrayList<GraphEdge<L>> rigaArchi : this.matrix)
			rigaArchi.remove(indiceCancellazione);
		// aggiorno gli indici della mappa
		this.aggiornaIndiciNodi(indiceCancellazione);
		return true;
	}

	@Override
	public boolean containsNode(GraphNode<L> node) {
		// controllo che il nodo sia null
		if (node == null)
			throw new NullPointerException("Il nodo è null");
		// controllo se il nodo è contenuto o meno
		return this.nodesIndex.containsKey(node);
	}

	@Override
	public GraphNode<L> getNodeOf(L label) {
		// controllo che la label sia null
		if (label == null)
			throw new NullPointerException("La label è null");
		// itero su tutti i nodi per vedere quale di quello ha la label
		for (GraphNode<L> nodoCorrente : this.nodesIndex.keySet()) {
			// controllo la label del nodo con quella passata
			if (nodoCorrente.getLabel().equals(label))
				return nodoCorrente;
		}
		// nodo non trovato
		return null;
	}

	@Override
	public int getNodeIndexOf(L label) {
		// trasformo la label nel nodo
		GraphNode<L> nodoCorrente = this.getNodeOf(label);
		// controllo che il nodo sia null
		if (nodoCorrente == null)
			throw new IllegalArgumentException("Il nodo è null");
		// prendo l'indice del nodo che appunto è il valore della mappa collegato alla
		// chiave nodoCorrente
		return this.nodesIndex.get(nodoCorrente);
	}

	@Override
	public GraphNode<L> getNodeAtIndex(int i) {
		// controllo che l'indice non sia negativo o non superi il numero dei nodi
		if (i < 0 || i >= this.nodeCount())
			throw new IndexOutOfBoundsException("L'indice è fuori portata");
		GraphNode<L> nodoRisultante = null;
		// itero sui nodi per vedere quale di essi ha l'indice
		for (GraphNode<L> nodoCorrente : this.nodesIndex.keySet()) {
			// se il nodo ha indice uguale ad i allora inizializzo il nodoRisultante
			if (this.nodesIndex.get(nodoCorrente).equals(i)) {
				nodoRisultante = nodoCorrente;
				break;
			}
		}
		// restituisco il nodoRisultante
		return nodoRisultante;
	}

	@Override
	public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
		// controllo che il nodo non sia già contenuto
		if (!this.containsNode(node))
			throw new IllegalArgumentException("Il nodo non è contenuto");
		// creo il Set
		Set<GraphNode<L>> insiemeAdiacenti = new HashSet<>();
		// prendo l'indice per scorrere sulla riga della matrice
		int i = this.nodesIndex.get(node);
		// scorro sulla riga della matrice
		for (int j = 0; j < this.nodeCount(); j++) {
			GraphEdge<L> arcoDellaCella = this.matrix.get(i).get(j);
			// se la cella è diversa da null allora prendo il nodo
			if (arcoDellaCella != null)
				insiemeAdiacenti.add(this.getNodeAtIndex(j));
		}
		return insiemeAdiacenti;
	}

	@Override
	public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
		throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
	}

	@Override
	public Set<GraphEdge<L>> getEdges() {
		// creo il Set
		Set<GraphEdge<L>> archiCorrenti = new HashSet<>();
		// scorro metà matrice (considerando pure i cappi)
		for (int i = 0; i < this.nodeCount(); i++) {
			for (int j = i; j < this.nodeCount(); j++) {
				GraphEdge<L> arcoDellaCella = this.matrix.get(i).get(j);
				// se la cella non è null allora la metto nella matrice
				if (arcoDellaCella != null)
					archiCorrenti.add(arcoDellaCella);
			}
		}
		// restituisco il set degli archi
		return archiCorrenti;

	}

	@Override
	public boolean addEdge(GraphEdge<L> edge) {
		// controllo l'arco
		this.controlloArchi(edge);
		// prendo i nodi collegati dall'arco
		GraphNode<L> nodoUno = edge.getNode1();
		GraphNode<L> nodoDue = edge.getNode2();
		// controllo che non siano contenuti
		if (!this.containsNode(nodoUno) || !this.containsNode(nodoDue))
			throw new IllegalArgumentException("I nodi dell'arco non sono contenuti");
		// prendo l'indice dei nodi
		int i = this.nodesIndex.get(nodoUno);
		int j = this.nodesIndex.get(nodoDue);
		// controllo che la cella non sia occupata
		if (this.matrix.get(i).get(j) != null)
			// se lo è restituisco false
			return false;
		// setto entrambe le celle con l'arco
		this.matrix.get(i).set(j, edge);
		this.matrix.get(j).set(i, edge);
		return true;
	}

	@Override
	public boolean removeEdge(GraphEdge<L> edge) {
		// controllo l'arco
		this.controlloArchi(edge);
		// prendo i nodi collegati dall'arco
		GraphNode<L> nodoUno = edge.getNode1();
		GraphNode<L> nodoDue = edge.getNode2();
		// controllo che non siano contenuti
		if (!this.containsNode(nodoUno) || !this.containsNode(nodoDue))
			return false;
		// prendo l'indice dei nodi
		int i = this.nodesIndex.get(nodoUno);
		int j = this.nodesIndex.get(nodoDue);
		// prendo l'arco che sta nella cella
		GraphEdge<L> arcoCorrente = this.matrix.get(i).get(j);
		// se è null oppure non è uguale al parametro return false
		if (arcoCorrente == null || !arcoCorrente.equals(edge))
			return false;
		// se invece il controllo non va a buon fine procedo con il rimpiazzo con null
		// tramite il metodo set
		this.matrix.get(i).set(j, null);
		this.matrix.get(j).set(i, null);

		return true;
	}

	@Override
	public boolean containsEdge(GraphEdge<L> edge) {
		// controllo l'arco
		this.controlloArchi(edge);
		// controllo che i nodi non siano contenuti
		if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2()))
			throw new IllegalArgumentException("I nodi dell'arco non sono contenuti");
		// prendo i loro indici
		int i = this.nodesIndex.get(edge.getNode1());
		int j = this.nodesIndex.get(edge.getNode2());
		// prendo la cella e verifico se c'è dentro edge
		GraphEdge<L> arcoCorrente = this.matrix.get(i).get(j);
		return (arcoCorrente != null && arcoCorrente.equals(edge));
	}

	@Override
	public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
		// controllo che il nodo non sia contenuto
		if (!this.containsNode(node))
			throw new IllegalArgumentException("Il nodo non è contenuto");
		// prendo un set dove mettere gli archi
		Set<GraphEdge<L>> archiIndividuati = new HashSet<>();
		// prendo la riga del nodo sulla matrice
		int i = this.nodesIndex.get(node);
		// scorro la riga
		for (int j = 0; j < this.nodeCount(); j++) {
			// se la cella della riga non contiene archi null allora vuol
			// dire che li posso mettere dentro il set
			GraphEdge<L> arcoDiAppoggio = this.matrix.get(i).get(j);
			if (arcoDiAppoggio != null)
				archiIndividuati.add(arcoDiAppoggio);
		}
		// restituiscso il set
		return archiIndividuati;
	}

	@Override
	public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
		throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
	}

	// creo dei metodi private per fini di implementazione

	/**
	 * Controllo che l'arco non sia null oppure non sia orientato
	 *
	 * @param arco da controllare
	 */

	private void controlloArchi(GraphEdge<L> arco) {
		// controllo che l'arco sia null
		if (arco == null)
			throw new NullPointerException("L'arco è null");
		// controllo che l'arco sia orientato
		if (arco.isDirected())
			throw new IllegalArgumentException("L'arco è orientato");
	}

	/**
	 * Aggiorna gli indici dei nodi presenti nella mappa nodesIndex
	 *
	 * @param spratiAcque è l'indice del nodo rimosso
	 */

	private void aggiornaIndiciNodi(int spratiAcque) {
		// aggiorno gli indici della mappa
		for (GraphNode<L> nodoCorrente : this.nodesIndex.keySet()) {
			int indiceAttuale = this.nodesIndex.get(nodoCorrente);
			// se l'indice del nodo è maggiore del indice del nodo eliminato allora lo
			// aggiorno
			if (indiceAttuale > spratiAcque)
				this.nodesIndex.put(nodoCorrente, indiceAttuale - 1);
		}
	}

}
