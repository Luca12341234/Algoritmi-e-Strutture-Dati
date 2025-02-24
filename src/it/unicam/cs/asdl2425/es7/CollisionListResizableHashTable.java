/**
 * 
 */
package it.unicam.cs.asdl2425.es7;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzione di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table= new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */
		if (o == null)
			throw new NullPointerException();
		if (this.isEmpty())
			return false;
		int posizione = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
		@SuppressWarnings("unchecked")
		Node<E> myNode = (Node<E>) this.table[posizione];
		while (myNode!= null) {
			if (myNode.item.equals(o))
				return true;
			myNode=myNode.next;
		}
//		for(Object obj : this.table) {
//			if(obj.equals(o)) return true;
//		}
		return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size >
        // this.getCurrentThreshold()
    	if (e == null)
			throw new NullPointerException();
    	int posizione = this.phf.hash(e.hashCode(),this.getCurrentCapacity());
    	if(this.contains(e)) return false;
    	//lista di torri e quindi posso metterla direttamente
    	this.table[posizione] = new Node<E>(e, (Node<E>) this.table[posizione]);
    	this.size++;
    	if(this.size>this.getCurrentThreshold()) this.resize();
    	this.modCount++;
    	return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        // TODO implementare
    	Object[] oldTable = this.table;
        this.table = new Object[oldTable.length * 2];
        for (Object obj : oldTable) {
            Node<E> node = (Node<E>) obj; 
            while (node != null) {
                int newIndex = this.phf.hash(node.item.hashCode(), this.table.length);
                this.table[newIndex] = new Node<E>(node.item, (Node<E>) this.table[newIndex]);
                node = node.next;
            }
        }
        this.modCount++;
    }

    @Override
    public boolean remove(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.
    	if (o == null)
			throw new NullPointerException();
    	int posizione = this.phf.hash(o.hashCode(),this.getCurrentCapacity());
    	Node<E> MyNode = (Node<E>) this.table[posizione];
        Node<E> precedente = null;
        while (MyNode != null) {
            if (MyNode.item.equals(o)) {
                if (precedente == null)
                    this.table[posizione] = MyNode.next;
                else 
                    precedente.next = MyNode.next;
                this.size--;
                this.modCount++;
                return true;
            }
            precedente = MyNode;
            MyNode = MyNode.next;
        }
    	return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo
        // contains
    	if(c==null) throw new NullPointerException();
     	Iterator<?> itr = c.iterator();
    	while(itr.hasNext()) {
    		Object item = itr.next();
    		if(!this.contains(item)) return false;
    	}
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo add
    	if(c==null) throw new NullPointerException();
    	Iterator<? extends E> itr = c.iterator();
    	boolean modified=false;
    	while(itr.hasNext()) {
    		E item = itr.next();
    		if(this.add(item)) modified=true;
    	}
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo remove
    	if(c==null) throw new NullPointerException();
    	Iterator<?> itr = c.iterator();
    	boolean modified=false;
    	while(itr.hasNext()) {
    		Object item = itr.next();
    		if(this.remove(item)) modified=true;
    	}
        return modified;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

        // TODO inserire le variabili che servono
    	private Node<E> lastReturned;
        private int numeroModificheAtteso;
        private int indiceDiIterazione;
        private Itr() {
            // TODO implementare il resto
        	this.lastReturned=null;
            this.numeroModificheAtteso = modCount;
            this.indiceDiIterazione=0;
        }

        @Override
        public boolean hasNext() {
            // TODO implementare
        	if (this.numeroModificheAtteso != modCount) {
                throw new ConcurrentModificationException("Errore di fail-fast!");
            }
            if (lastReturned != null && lastReturned.next != null) 
                return true;
            while (indiceDiIterazione < getCurrentCapacity() && table[indiceDiIterazione] == null)
                indiceDiIterazione++;
            return indiceDiIterazione < getCurrentCapacity();
        }

        @Override
        public E next() {
            // TODO implementare
        	if (this.numeroModificheAtteso != modCount) 
				throw new ConcurrentModificationException(""
						+ "Errore di fail-fast!");
        	if (!hasNext())
				throw new NoSuchElementException(""
						+ "Siamo arrivati al capo linea");
        	//devo stare attento con il return
        	//o itera nella torre o nelle caselle
            if(lastReturned==null || lastReturned.next==null) {
            	lastReturned=(Node<E>)table[indiceDiIterazione++];
            }else {
            	lastReturned=lastReturned.next;
            }
        	
        	return lastReturned.item;
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
