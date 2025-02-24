/**
 * 
 */
package it.unicam.cs.asdl2425.es11;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        // TODO implementare
    	return adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        // TODO implementare
    	Set<GraphEdge<L>> allEdges = new HashSet<>();
        for (Set<GraphEdge<L>> edgesSet : adjacentLists.values()) {
            allEdges.addAll(edgesSet);
        }
        return allEdges.size();
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
    	boolean[] risultato = {false};

        this.adjacentLists.forEach((n, edges) -> {
            for (GraphEdge<L> edge : edges) {
                if (edge.isDirected()) {
                    risultato[0] = true;
                }
            }
        });

        return risultato[0];
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // TODO implementare
    	return new HashSet<>(adjacentLists.keySet());
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        // TODO implementare
    	if (node == null) {
            throw new NullPointerException("Tentativo di aggiungere un nodo null");
        }
        if (adjacentLists.containsKey(node)) {
            return false; // già presente
        }
        adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        // TODO implementare
    	if (node == null) {
            throw new NullPointerException("Nodo null");
        }
        return adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        // TODO implementare
    	if (label == null) {
            throw new NullPointerException("Etichetta null");
        }
        for (GraphNode<L> node : adjacentLists.keySet()) {
            if (label.equals(node.getLabel())) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // TODO implementare
    	if (node == null) {
            throw new NullPointerException("Nodo null");
        }
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");
        }
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        for (GraphEdge<L> edge : adjacentLists.get(node)) {
            // Nell'arco non orientato l'altro nodo è quello che non è "node"
            if (edge.getNode1().equals(node)) {
                adjacentNodes.add(edge.getNode2());
            } else {
                adjacentNodes.add(edge.getNode1());
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        // TODO implementare
    	return getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // TODO implementare
    	Set<GraphEdge<L>> allEdges = new HashSet<>();
        for (Set<GraphEdge<L>> edgesSet : adjacentLists.values()) {
            allEdges.addAll(edgesSet);
        }
        return allEdges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // TODO implementare
    	if (edge == null) {
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        }
        // Per questo grafo l'arco deve essere non orientato.
        if (edge.isDirected()) {
            throw new IllegalArgumentException("Arco orientato non ammesso in un grafo non orientato");
        }
        
        GraphNode<L> n1 = edge.getNode1();
        GraphNode<L> n2 = edge.getNode2();
        
        // Entrambi i nodi devono già esistere nel grafo.
        if (!this.containsNode(n1) || !this.containsNode(n2)) {
            throw new IllegalArgumentException("Almeno uno dei nodi dell'arco non esiste nel grafo");
        }
        
        // Recupera gli insiemi degli archi per i due nodi.
        Set<GraphEdge<L>> edgesN1 = adjacentLists.get(n1);
        Set<GraphEdge<L>> edgesN2 = adjacentLists.get(n2);
        
        // Se l'arco è già presente (in uno dei due set) allora non viene inserito.
        if (edgesN1.contains(edge) || edgesN2.contains(edge)) {
            return false;
        }
        
        // Poiché il grafo è non orientato, aggiungiamo l'arco in entrambi i set.
        edgesN1.add(edge);
        edgesN2.add(edge);
        
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        // TODO implementare
    	if (edge == null) {
            throw new NullPointerException("Arco null");
        }
        // Verifica che entrambi i nodi siano presenti nel grafo
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) {
            throw new IllegalArgumentException("Almeno uno dei nodi dell'arco non esiste nel grafo");
        }
        // Verifica se l'arco è presente nella lista del primo nodo
        return adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // TODO implementare
    	if (node == null) {
            throw new NullPointerException("Nodo null");
        }
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");
        }
        // Restituisce una copia dell'insieme degli archi adiacenti
        return new HashSet<>(adjacentLists.get(node));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
