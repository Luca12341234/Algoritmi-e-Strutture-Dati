package it.unicam.cs.asdl2425.pt1;

import java.util.ArrayList;
import java.util.List;

/**
 * Un AVLTree è un albero binario di ricerca che si mantiene sempre bilanciato.
 * In questa particolare classe si possono inserire elementi ripetuti di tipo
 * {@code E} e non si possono inserire elementi {@code null}.
 * 
 * @param <E>
 *              il tipo degli elementi che possono essere inseriti in questo
 *              AVLTree. La classe {@code E} deve avere un ordinamento naturale
 *              definito tra gli elementi.
 * 
 * @author Luca Tesei (template) 
 *         // TODO INSERIRE NOME, COGNOME ED EMAIL xxxx@studenti.unicam.it DELLO STUDENTE
 */
public class AVLTree<E extends Comparable<E>> {

    // puntatore al nodo radice, se questo puntatore è null allora questo AVLTree è vuoto
    private AVLTreeNode root;

    // Numero di elementi inseriti in questo AVLTree, comprese le ripetizioni
    private int size;

    // Numero di nodi in questo AVLTree
    private int numberOfNodes;

    /**
     * Costruisce un AVLTree vuoto.
     */
    public AVLTree() {
        this.root = null;
        this.size = 0;
        this.numberOfNodes = 0;
    }

    /**
     * Costruisce un AVLTree che consiste solo di un nodo radice.
     * 
     * @param rootElement l'informazione associata al nodo radice
     * @throws NullPointerException se l'elemento passato è null
     */
    public AVLTree(E rootElement) {
        if(rootElement == null)
            throw new NullPointerException("Elemento radice nullo");
        this.root = new AVLTreeNode(rootElement);
        this.size = 1;
        this.numberOfNodes = 1;
    }

    /**
     * Determina se questo AVLTree è vuoto.
     * 
     * @return true, se questo AVLTree è vuoto.
     */
    public boolean isEmpty() {
        return (this.root == null);
    }

    /**
     * Restituisce il numero di elementi contenuti in questo AVLTree. In caso di
     * elementi ripetuti essi vengono contati più volte.
     * 
     * @return il numero di elementi di tipo {@code E} presenti in questo AVLTree.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Restituisce il numero di nodi in questo AVLTree.
     * 
     * @return il numero di nodi in questo AVLTree.
     */
    public int getNumberOfNodes() {
        return this.numberOfNodes;
    }

    /**
     * Restituisce l'altezza di questo AVLTree. Se questo AVLTree è vuoto viene
     * restituito il valore -1.
     * 
     * @return l'altezza di questo AVLTree, -1 se questo AVLTree è vuoto.
     */
    public int getHeight() {
        return (root == null ? -1 : root.getHeight());
    }

    /**
     * @return the root
     */
    public AVLTreeNode getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(AVLTreeNode root) {
        this.root = root;
    }

    /**
     * Determina se questo AVLTree è bilanciato. L'albero è bilanciato se tutti
     * i nodi hanno un fattore di bilanciamento compreso tra -1 e +1.
     * 
     * @return true, se il fattore di bilanciamento di tutti i nodi dell'albero è compreso tra -1 e +1.
     */
    public boolean isBalanced() {
        return (root == null || root.isBalanced());
    }

    /**
     * Inserisce un nuovo elemento in questo AVLTree. Se l'elemento è già
     * presente viene incrementato il suo numero di occorrenze.
     * 
     * @param el l'elemento da inserire.
     * @return il numero di confronti tra elementi della classe {@code E} effettuati durante l'inserimento
     * @throws NullPointerException se l'elemento {@code el} è null
     */
    public int insert(E el) {
        if (el == null)
            throw new NullPointerException("Elemento nullo non ammesso");
        // Se l'albero è vuoto, crea la radice.
        if (this.root == null) {
            this.root = new AVLTreeNode(el);
            this.size = 1;
            this.numberOfNodes = 1;
            return 1; // Un confronto per verificare che l'albero sia vuoto.
        }
        ComparisonCounter counter = new ComparisonCounter();
        // L'inserimento restituisce il nuovo sott'albero radice; se la radice cambia, aggiornala.
        this.root = this.root.insert(el, counter);
        // Aggiorna la dimensione: se l'elemento era già presente, il metodo ha incrementato solo la molteplicità,
        // altrimenti viene creato un nuovo nodo.
        this.size++; // in entrambi i casi incrementa il numero di elementi
        // Se l'inserimento ha creato un nuovo nodo (la molteplicità era 1 al momento della creazione), incrementa numberOfNodes.
        // Per semplicità, nel metodo insert dei nodi, quando viene creato un nuovo nodo si incrementa numberOfNodes tramite
        // il riferimento all'albero (vedi sotto).
        return counter.count;
    }

    /**
     * Determina se questo AVLTree contiene un certo elemento.
     * 
     * @param el l'elemento da cercare
     * @return true se l'elemento è presente in questo AVLTree, false altrimenti.
     * @throws NullPointerException se l'elemento {@code el} è null
     */
    public boolean contains(E el) {
        if (el == null)
            throw new NullPointerException("Elemento nullo");
        return (this.root != null && this.root.search(el) != null);
    }

    /**
     * Determina se un elemento è presente in questo AVLTree e ne restituisce il relativo nodo.
     * 
     * @param el l'elemento da cercare
     * @return il nodo di questo AVLTree che contiene l'elemento {@code el} e la sua molteplicità, oppure {@code null} se l'elemento non è presente.
     * @throws NullPointerException se l'elemento {@code el} è null
     */
    public AVLTreeNode getNodeOf(E el) {
        if (el == null)
            throw new NullPointerException("Elemento nullo");
        return (this.root == null ? null : this.root.search(el));
    }

    /**
     * Determina il numero di occorrenze di un certo elemento in questo AVLTree.
     * 
     * @param el l'elemento di cui determinare il numero di occorrenze
     * @return il numero di occorrenze dell'elemento {@code el} in questo AVLTree, zero se non è presente.
     * @throws NullPointerException se l'elemento {@code el} è null
     */
    public int getCount(E el) {
        AVLTreeNode node = getNodeOf(el);
        return (node == null ? 0 : node.getCount());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (root == null)
            return "AVLTree vuoto";
        String descr = "AVLTree [root=" + root.el.toString() + ", size=" + size
                + ", numberOfNodes=" + numberOfNodes + "]\n";
        return descr + this.root.toString();
    }

    /**
     * Restituisce la lista degli elementi contenuti in questo AVLTree secondo
     * l'ordine determinato dalla visita in-order.
     * 
     * @return la lista ordinata degli elementi contenuti in questo AVLTree, tenendo conto della loro molteplicità.
     */
    public List<E> inOrderVisit() {
        List<E> list = new ArrayList<E>();
        if (root != null)
            root.inOrder(list);
        return list;
    }

    /**
     * Restituisce l'elemento minimo presente in questo AVLTree.
     * 
     * @return l'elemento minimo in questo AVLTree, {@code null} se questo AVLTree è vuoto.
     */
    public E getMinimum() {
        if (root == null)
            return null;
        return root.getMinimum().getEl();
    }

    /**
     * Restituisce l'elemento massimo presente in questo AVLTree.
     * 
     * @return l'elemento massimo in questo AVLTree, {@code null} se questo AVLTree è vuoto.
     */
    public E getMaximum() {
        if (root == null)
            return null;
        return root.getMaximum().getEl();
    }

    /**
     * Restituisce l'elemento <b>strettamente</b> successore, in questo AVLTree,
     * di un dato elemento. Si richiede che l'elemento passato sia presente nell'albero.
     * 
     * @param el l'elemento di cui si chiede il successore
     * @return l'elemento <b>strettamente</b> successore di {@code el} nell'albero, oppure {@code null} se {@code el} è il massimo.
     * @throws NullPointerException se l'elemento {@code el} è null
     * @throws IllegalArgumentException se l'elemento {@code el} non è presente nell'albero.
     */
    public E getSuccessor(E el) {
        if (el == null)
            throw new NullPointerException("Elemento nullo");
        AVLTreeNode node = getNodeOf(el);
        if (node == null)
            throw new IllegalArgumentException("Elemento non presente nell'albero");
        AVLTreeNode succ = node.getSuccessor();
        return (succ == null ? null : succ.getEl());
    }

    /**
     * Restituisce l'elemento <b>strettamente</b> predecessore, in questo AVLTree,
     * di un dato elemento. Si richiede che l'elemento passato sia presente nell'albero.
     * 
     * @param el l'elemento di cui si chiede il predecessore
     * @return l'elemento <b>strettamente</b> predecessore di {@code el} nell'albero, oppure {@code null} se {@code el} è il minimo.
     * @throws NullPointerException se l'elemento {@code el} è null
     * @throws IllegalArgumentException se l'elemento {@code el} non è presente nell'albero.
     */
    public E getPredecessor(E el) {
        if (el == null)
            throw new NullPointerException("Elemento nullo");
        AVLTreeNode node = getNodeOf(el);
        if (node == null)
            throw new IllegalArgumentException("Elemento non presente nell'albero");
        AVLTreeNode pred = node.getPredecessor();
        return (pred == null ? null : pred.getEl());
    }

    /**
     * Helper per contare i confronti durante l'inserimento.
     */
    private class ComparisonCounter {
        int count = 0;
    }

    /**
     * Gli elementi di questa classe sono i nodi di un AVLTree.
     */
    public class AVLTreeNode {
        // etichetta del nodo
        private E el;
        // molteplicità dell'elemento el
        private int count;
        // sottoalbero sinistro
        private AVLTreeNode left;
        // sottoalbero destro
        private AVLTreeNode right;
        // genitore del nodo, null se questo nodo è la radice dell'AVLTree
        private AVLTreeNode parent;
        // altezza del sottoalbero avente questo nodo come radice
        private int height;

        /**
         * Crea un AVLTreeNode come foglia radice.
         * 
         * @param el the element
         */
        public AVLTreeNode(E el) {
            this(el, null);
        }

        /**
         * Crea un AVLTreeNode come figlio.
         * 
         * @param el the element
         * @param parent il genitore del nodo
         */
        public AVLTreeNode(E el, AVLTreeNode parent) {
            if (el == null)
                throw new NullPointerException("Elemento nullo non ammesso");
            this.el = el;
            this.count = 1;
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.height = 0; // foglia: altezza 0
            // Se viene creato un nuovo nodo, aggiorna il numero di nodi nell'albero
            numberOfNodes++;
        }

        /**
         * Restituisce il nodo predecessore di questo nodo.
         * Si suppone che esista (non è il minimo).
         */
        public AVLTreeNode getPredecessor() {
            // Se esiste un sottoalbero sinistro, il predecessore è il massimo di quel sottoalbero
            if (this.left != null)
                return this.left.getMaximum();
            // Altrimenti, risali finché questo nodo non è un figlio destro
            AVLTreeNode curr = this;
            AVLTreeNode p = curr.parent;
            while (p != null && curr == p.left) {
                curr = p;
                p = p.parent;
            }
            return p;
        }

        /**
         * Restituisce il nodo successore di questo nodo.
         * Si suppone che esista (non è il massimo).
         */
        public AVLTreeNode getSuccessor() {
            if (this.right != null)
                return this.right.getMinimum();
            AVLTreeNode curr = this;
            AVLTreeNode p = curr.parent;
            while (p != null && curr == p.right) {
                curr = p;
                p = p.parent;
            }
            return p;
        }

        /**
         * Restituisce il nodo contenente l'elemento massimo del sottoalbero.
         */
        public AVLTreeNode getMaximum() {
            AVLTreeNode curr = this;
            while (curr.right != null)
                curr = curr.right;
            return curr;
        }

        /**
         * Restituisce il nodo contenente l'elemento minimo del sottoalbero.
         */
        public AVLTreeNode getMinimum() {
            AVLTreeNode curr = this;
            while (curr.left != null)
                curr = curr.left;
            return curr;
        }

        /**
         * Determina se questo nodo è una foglia.
         */
        public boolean isLeaf() {
            return (this.left == null && this.right == null);
        }

        /**
         * Restituisce l'altezza del sottoalbero radicato in questo nodo.
         */
        public int getHeight() {
            return this.height;
        }

        /**
         * Aggiorna l'altezza del nodo, considerando le altezze dei figli.
         */
        public void updateHeight() {
            int leftHeight = (left == null ? -1 : left.height);
            int rightHeight = (right == null ? -1 : right.height);
            this.height = 1 + Math.max(leftHeight, rightHeight);
        }

        /**
         * Calcola il fattore di bilanciamento del nodo.
         * 
         * @return (altezza del figlio sinistro) - (altezza del figlio destro)
         */
        public int getBalanceFactor() {
            int leftHeight = (left == null ? -1 : left.height);
            int rightHeight = (right == null ? -1 : right.height);
            return leftHeight - rightHeight;
        }

        /**
         * Determina se questo nodo e tutti i suoi discendenti sono bilanciati.
         */
        public boolean isBalanced() {
            int bf = getBalanceFactor();
            if (bf < -1 || bf > 1)
                return false;
            boolean leftBalanced = (left == null ? true : left.isBalanced());
            boolean rightBalanced = (right == null ? true : right.isBalanced());
            return leftBalanced && rightBalanced;
        }

        /**
         * Restituisce l'elemento memorizzato in questo nodo.
         */
        public E getEl() {
            return this.el;
        }

        /**
         * Imposta l'elemento memorizzato in questo nodo.
         */
        public void setEl(E el) {
            this.el = el;
        }

        /**
         * Restituisce il numero di occorrenze dell'elemento memorizzato in questo nodo.
         */
        public int getCount() {
            return this.count;
        }

        /**
         * Imposta il numero di occorrenze.
         */
        public void setCount(int count) {
            this.count = count;
        }

        /**
         * Restituisce il figlio sinistro.
         */
        public AVLTreeNode getLeft() {
            return this.left;
        }

        /**
         * Imposta il figlio sinistro.
         */
        public void setLeft(AVLTreeNode left) {
            this.left = left;
            if (left != null)
                left.parent = this;
        }

        /**
         * Restituisce il figlio destro.
         */
        public AVLTreeNode getRight() {
            return this.right;
        }

        /**
         * Imposta il figlio destro.
         */
        public void setRight(AVLTreeNode right) {
            this.right = right;
            if (right != null)
                right.parent = this;
        }

        /**
         * Restituisce il genitore.
         */
        public AVLTreeNode getParent() {
            return this.parent;
        }

        /**
         * Imposta il genitore.
         */
        public void setParent(AVLTreeNode parent) {
            this.parent = parent;
        }

        /**
         * Visita in-order e aggiunge gli elementi alla lista (tenendo conto della molteplicità).
         */
        public void inOrder(List<E> list) {
            if (this.left != null)
                this.left.inOrder(list);
            for (int i = 0; i < this.count; i++) {
                list.add(this.el);
            }
            if (this.right != null)
                this.right.inOrder(list);
        }

        /**
         * Ricerca un elemento a partire da questo nodo.
         * 
         * @param el l'elemento da cercare
         * @return il nodo che contiene l'elemento oppure null se non trovato
         */
        public AVLTreeNode search(E el) {
            int cmp = el.compareTo(this.el);
            if (cmp == 0)
                return this;
            else if (cmp < 0)
                return (left == null ? null : left.search(el));
            else
                return (right == null ? null : right.search(el));
        }

        /**
         * Inserisce un elemento nell'albero AVL a partire da questo nodo.
         * Se l'elemento è già presente, incrementa la molteplicità;
         * altrimenti inserisce un nuovo nodo e procede al bilanciamento.
         * 
         * @param el l'elemento da inserire
         * @param counter oggetto per contare i confronti
         * @return il nuovo nodo radice del sottoalbero (dopo eventuali rotazioni)
         */
        public AVLTreeNode insert(E el, ComparisonCounter counter) {
            int cmp = el.compareTo(this.el);
            counter.count++;
            if (cmp == 0) {
                // Elemento già presente: incrementa il count
                this.count++;
                return this;
            } else if (cmp < 0) {
                // Inserimento nel sottoalbero sinistro
                if (this.left == null) {
                    this.left = new AVLTreeNode(el, this);
                } else {
                    this.left = this.left.insert(el, counter);
                }
            } else {
                // Inserimento nel sottoalbero destro
                if (this.right == null) {
                    this.right = new AVLTreeNode(el, this);
                } else {
                    this.right = this.right.insert(el, counter);
                }
            }
            // Dopo l'inserimento, aggiorna l'altezza
            updateHeight();
            // Bilancia il nodo se necessario
            return balance();
        }

        /**
         * Effettua il bilanciamento del nodo se necessario, mediante le rotazioni.
         * 
         * @return il nuovo nodo radice del sottoalbero (potenzialmente cambiato)
         */
        private AVLTreeNode balance() {
            int bf = getBalanceFactor();
            // Caso sinistra pesante
            if (bf > 1) {
                // Se il figlio sinistro ha bilanciamento non negativo: rotazione a destra
                if (left.getBalanceFactor() >= 0) {
                    return rotateRight();
                } else {
                    // Altrimenti rotazione sinistra-destra
                    this.left = this.left.rotateLeft();
                    return rotateRight();
                }
            }
            // Caso destra pesante
            if (bf < -1) {
                if (right.getBalanceFactor() <= 0) {
                    return rotateLeft();
                } else {
                    // Rotazione destra-sinistra
                    this.right = this.right.rotateRight();
                    return rotateLeft();
                }
            }
            // Se il nodo è già bilanciato, restituiscilo
            return this;
        }

        /**
         * Rotazione a destra.
         * 
         * @return il nuovo nodo radice dopo la rotazione
         */
        private AVLTreeNode rotateRight() {
            AVLTreeNode newRoot = this.left;
            this.left = newRoot.right;
            if (newRoot.right != null)
                newRoot.right.parent = this;
            newRoot.right = this;
            newRoot.parent = this.parent;
            this.parent = newRoot;
            // Aggiorna le altezze
            this.updateHeight();
            newRoot.updateHeight();
            return newRoot;
        }

        /**
         * Rotazione a sinistra.
         * 
         * @return il nuovo nodo radice dopo la rotazione
         */
        private AVLTreeNode rotateLeft() {
            AVLTreeNode newRoot = this.right;
            this.right = newRoot.left;
            if (newRoot.left != null)
                newRoot.left.parent = this;
            newRoot.left = this;
            newRoot.parent = this.parent;
            this.parent = newRoot;
            // Aggiorna le altezze
            this.updateHeight();
            newRoot.updateHeight();
            return newRoot;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("(");
            s.append(this.el);
            s.append(", ");
            s.append(this.left == null ? "()" : this.left.toString());
            s.append(", ");
            s.append(this.right == null ? "()" : this.right.toString());
            s.append(")");
            return s.toString();
        }
    }
}
