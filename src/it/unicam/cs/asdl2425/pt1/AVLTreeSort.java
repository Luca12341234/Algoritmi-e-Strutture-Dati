package it.unicam.cs.asdl2425.pt1;

import java.util.List;
import java.util.ArrayList;

/**
 * Algoritmo di ordinamento che usa un albero AVL con molteplicità per ordinare
 * una lista di elementi. La strategia di realizzazione è semplice: si
 * inseriscono i valori da ordinare in un AVLTree e poi si fa una visita inOrder
 * per ottenere la lista ordinata di elementi.
 * 
 * @author Luca Tesei (template)
 *         **INSERIRE NOME, COGNOME ED EMAIL xxxx@studenti.unicam.it DELLO STUDENTE**
 */
public class AVLTreeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    /**
     * Nodo dell'albero AVL. Ogni nodo contiene un elemento, la sua molteplicità,
     * l'altezza e i riferimenti ai figli sinistro e destro.
     */
    private class Node {
        E key;
        int count;   // molteplicità dell'elemento (per gestire i duplicati)
        int height;
        Node left;
        Node right;

        Node(E key) {
            this.key = key;
            this.count = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    /**
     * Classe helper per contare il numero di confronti effettuati.
     */
    private class Counter {
        long count = 0;
    }

    /**
     * Esegue l'ordinamento in loco della lista passata usando un albero AVL con
     * molteplicità. Per ogni elemento della lista viene effettuato un inserimento
     * nell'albero e successivamente, mediante una visita in-order, si ottiene la lista
     * ordinata.
     * 
     * Se la lista è null o contiene elementi null, viene lanciata una
     * NullPointerException.
     * 
     * @param l la lista di elementi da ordinare (non null e senza elementi null)
     * @return un oggetto SortingAlgorithmResult contenente la lista ordinata e il numero
     *         di confronti effettuati
     */
    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if (l == null)
            throw new NullPointerException("La lista da ordinare non può essere null");
        
        // Controllo che nessun elemento sia null
        for (E element : l) {
            if (element == null)
                throw new NullPointerException("La lista non può contenere elementi null");
        }

        Counter counter = new Counter();
        Node root = null;
        // Inserisce tutti gli elementi nell'albero AVL
        for (E element : l) {
            root = insert(root, element, counter);
        }

        // Esegue la visita in-order per ottenere la lista ordinata
        List<E> sortedList = new ArrayList<>();
        inOrder(root, sortedList);

        return new SortingAlgorithmResult<>(sortedList, (int) counter.count);
    }

    /**
     * Inserisce ricorsivamente un nuovo elemento nell'albero AVL, aggiornando
     * il contatore dei confronti.
     * 
     * @param node    il nodo corrente dell'albero
     * @param key     l'elemento da inserire
     * @param counter il contatore dei confronti
     * @return il nodo aggiornato
     */
    private Node insert(Node node, E key, Counter counter) {
        if (node == null) {
            return new Node(key);
        }
        // Effettua il confronto tra key e il valore del nodo corrente
        int cmp = key.compareTo(node.key);
        counter.count++; // conta questo confronto
        if (cmp == 0) {
            // Elemento già presente: incrementa la molteplicità
            node.count++;
            return node;
        } else if (cmp < 0) {
            node.left = insert(node.left, key, counter);
        } else { // cmp > 0
            node.right = insert(node.right, key, counter);
        }

        // Aggiorna l'altezza del nodo corrente
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Calcola il fattore di bilanciamento
        int balance = getBalance(node);

        // Verifica e corregge eventuali sbilanciamenti
        // Caso Left Left
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            counter.count++; // confronto effettuato per la condizione
            return rightRotate(node);
        }
        // Caso Left Right
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            counter.count++; // confronto effettuato per la condizione
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // Caso Right Right
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            counter.count++; // confronto effettuato per la condizione
            return leftRotate(node);
        }
        // Caso Right Left
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            counter.count++; // confronto effettuato per la condizione
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    /**
     * Restituisce l'altezza del nodo.
     */
    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    /**
     * Calcola il fattore di bilanciamento del nodo.
     */
    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    /**
     * Esegue una rotazione a destra.
     */
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Esegue la rotazione
        x.right = y;
        y.left = T2;

        // Aggiorna le altezze
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    /**
     * Esegue una rotazione a sinistra.
     */
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Esegue la rotazione
        y.left = x;
        x.right = T2;

        // Aggiorna le altezze
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    /**
     * Esegue la visita in-order dell'albero AVL per ottenere la lista ordinata.
     */
    private void inOrder(Node node, List<E> sorted) {
        if (node != null) {
            inOrder(node.left, sorted);
            // Aggiunge l'elemento tante volte quanta è la sua molteplicità
            for (int i = 0; i < node.count; i++) {
                sorted.add(node.key);
            }
            inOrder(node.right, sorted);
        }
    }

    /**
     * Restituisce il nome dell'algoritmo di ordinamento.
     */
    @Override
    public String getName() {
        return "AVLTreeSort";
    }
}
