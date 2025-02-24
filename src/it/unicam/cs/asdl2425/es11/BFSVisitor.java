package it.unicam.cs.asdl2425.es11;
import java.util.LinkedList;
import java.util.Queue;
/**
 * Classe singoletto che fornisce lo schema generico di visita Breadth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class BFSVisitor<L> {

    /**
     * Esegue la visita in ampiezza di un certo grafo a partire da un nodo
     * sorgente. Setta i valori seguenti valori associati ai nodi: distanza
     * intera, predecessore. La distanza indica il numero minimo di archi che si
     * devono percorrere dal nodo sorgente per raggiungere il nodo e il
     * predecessore rappresenta il padre del nodo in un albero di copertura del
     * grafo. Ogni volta che un nodo viene visitato viene eseguito il metodo
     * visitNode sul nodo. In questa classe il metodo non fa niente, basta
     * creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *                   il grafo da visitare.
     * @param source
     *                   il nodo sorgente.
     * @throws NullPointerException
     *                                      se almeno un valore passato è null
     * @throws IllegalArgumentException
     *                                      se il nodo sorgente non appartiene
     *                                      al grafo dato
     */
    public void BFSVisit(Graph<L> g, GraphNode<L> source) {
        // TODO implementare
        // NOTA: chiamare il metodo visitNode quando un nodo passa da grigio a nero
    	if (g == null || source == null) {
            throw new NullPointerException("Il grafo o il nodo sorgente sono null.");
        }
        if (!g.containsNode(source)) {
            throw new IllegalArgumentException("Il nodo sorgente non appartiene al grafo.");
        }
        
        // Inizializza tutti i nodi: colore bianco, distanza infinita (Integer.MAX_VALUE) e predecessore null.
        for (GraphNode<L> node : g.getNodes()) {
            node.setColor(GraphNode.COLOR_WHITE);
            node.setIntegerDistance(Integer.MAX_VALUE);
            node.setPrevious(null);
        }
        
        // Imposta il nodo sorgente
        source.setColor(GraphNode.COLOR_GREY);
        source.setIntegerDistance(0);
        
        Queue<GraphNode<L>> queue = new LinkedList<>();
        queue.offer(source);
        
        while (!queue.isEmpty()) {
            GraphNode<L> u = queue.poll();
            // Esplora tutti i nodi adiacenti
            for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
                if (v.getColor() == GraphNode.COLOR_WHITE) {
                    v.setColor(GraphNode.COLOR_GREY);
                    v.setIntegerDistance(u.getIntegerDistance() + 1);
                    v.setPrevious(u);
                    queue.offer(v);
                }
            }
            u.setColor(GraphNode.COLOR_BLACK);
            // Chiamata al metodo per azioni specifiche (default non fa nulla)
            visitNode(u);
        }
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la BFS quando i nodi passano da grigio a nero.
     * Ridefinire il metodo in una sottoclasse per effettuare azioni specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}
