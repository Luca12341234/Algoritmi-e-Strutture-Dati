package it.unicam.cs.asdl2425.es11;
/**
 * Classe singoletto che fornisce lo schema generico di visita Depth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class DFSVisitor<L> {

    // Variabile "globale" per far andare avanti il tempo durante la DFS e
    // assegnare i relativi tempi di scoperta e di uscita dei nodi
    // E' protected per permettere il test JUnit
    protected int time;

    /**
     * Esegue la visita in profondità di un certo grafo. Setta i valori seguenti
     * valori associati ai nodi: tempo di scoperta, tempo di fine visita,
     * predecessore. Ogni volta che un nodo viene visitato viene eseguito il
     * metodo visitNode sul nodo. In questa classe il metodo non fa niente,
     * basta creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *              il grafo da visitare.
     * @throws NullPointerException
     *                                  se il grafo passato è null
     */
    public void DFSVisit(Graph<L> g) {
        // TODO implementare
        // NOTA: inizializza il grafo e chiama la recDFS sui nodi in un ordine
        // qualsiasi per calcolare la "foresta" DFS
    	if (g == null) {
            throw new NullPointerException("Il grafo è null.");
        }
        
        // Inizializza tutti i nodi: colore bianco e predecessore null.
        for (GraphNode<L> node : g.getNodes()) {
            node.setColor(GraphNode.COLOR_WHITE);
            node.setPrevious(null);
        }
        
        time = 0;
        
        // Per ogni nodo (in ordine arbitrario) se non ancora visitato esegui la DFS
        for (GraphNode<L> node : g.getNodes()) {
            if (node.getColor() == GraphNode.COLOR_WHITE) {
                recDFS(g, node);
            }
        }
    }

    /*
     * Esegue la DFS ricorsivamente sul nodo passato.
     * 
     * @param g il grafo
     * 
     * @param u il nodo su cui parte la DFS
     */
    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        // TODO implementare ricorsivamente
        // NOTA: chiamare il metodo visitNode alla "scoperta" di un nuovo nodo
    	time++;
        u.setEnteringTime(time);
        u.setColor(GraphNode.COLOR_GREY);
        
        // Esplora i nodi adiacenti
        for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
            if (v.getColor() == GraphNode.COLOR_WHITE) {
                v.setPrevious(u);
                recDFS(g, v);
            }
        }
        
        u.setColor(GraphNode.COLOR_BLACK);
        time++;
        u.setExitingTime(time);
        // Chiamata al metodo per azioni specifiche (default non fa nulla)
        visitNode(u);
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la DFS nel momento in cui il colore passa da grigio
     * a nero. Ridefinire il metodo in una sottoclasse per effettuare azioni
     * specifiche.
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
