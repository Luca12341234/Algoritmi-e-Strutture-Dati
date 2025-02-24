package it.unicam.cs.asdl2425.mp1;

import java.util.ArrayList;
import java.util.Iterator;

// inseriti import per lanciare eccezioni dell'Iterator

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Una classe che rappresenta una lista concatenata con il calcolo degli hash
 * MD5 per ciascun elemento. Ogni nodo della lista contiene il dato originale di
 * tipo generico T e il relativo hash calcolato utilizzando l'algoritmo MD5.
 *
 * <p>
 * La classe supporta le seguenti operazioni principali:
 * <ul>
 * <li>Aggiungere un elemento in testa alla lista</li>
 * <li>Aggiungere un elemento in coda alla lista</li>
 * <li>Rimuovere un elemento dalla lista in base al dato</li>
 * <li>Recuperare una lista ordinata di tutti gli hash contenuti nella
 * lista</li>
 * <li>Costruire una rappresentazione testuale della lista</li>
 * </ul>
 *
 * <p>
 * Questa implementazione include ottimizzazioni come il mantenimento di un
 * riferimento all'ultimo nodo della lista (tail), che rende l'inserimento in
 * coda un'operazione O(1).
 *
 * <p>
 * La classe utilizza la classe HashUtil per calcolare l'hash MD5 dei dati.
 *
 * @param <T> il tipo generico dei dati contenuti nei nodi della lista.
 * 
 * @author Luca Tesei, Marco Caputo (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 * 
 */
public class HashLinkedList<T> implements Iterable<T> {
	private Node head; // Primo nodo della lista

	private Node tail; // Ultimo nodo della lista

	private int size; // Numero di nodi della lista

	private int numeroModifiche; // Numero di modifiche effettuate sulla lista
									// per l'implementazione dell'iteratore
									// fail-fast

	public HashLinkedList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
		this.numeroModifiche = 0;
	}

	/**
	 * Restituisce il numero attuale di nodi nella lista.
	 *
	 * @return il numero di nodi nella lista.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Rappresenta un nodo nella lista concatenata.
	 */
	private class Node {
		String hash; // Hash del dato

		T data; // Dato originale

		Node next;

		Node(T data) {
			this.data = data;
			this.hash = HashUtil.dataToHash(data);
			this.next = null;
		}
	}

	/**
	 * Aggiunge un nuovo elemento in testa alla lista.
	 *
	 * @param data il dato da aggiungere.
	 */
	public void addAtHead(T data) {
		if (data == null)
			throw new IllegalArgumentException("Non puoi inserire un dato nullo");
		// Aggiungo un nuovo nodo all'inizio della lista collegandolo alla testa
		Node nodoPerFareSwap = new Node(data);
		nodoPerFareSwap.next = this.head;
		this.head = nodoPerFareSwap;
		// gestisco il caso in cui devo inserire il primo nodo
		if (this.tail == null)
			this.tail = this.head;

		// aumento la dimensione ed il numero delle modifiche
		this.numeroModifiche++;
		this.size++;
	}

	/**
	 * Aggiunge un nuovo elemento in coda alla lista.
	 *
	 * @param data il dato da aggiungere.
	 */
	public void addAtTail(T data) {
		// controllo che la variabile data non sia null
		if (data == null)
			throw new IllegalArgumentException("Non puoi inserire un dato nullo");
		// creo un nodo di appoggio
		Node nodoPerFareSwap = new Node(data);
		// se la coda è nulla allora la inserisco con la testa
		if (this.tail == null)
			// inizializzo la testa
			this.head = nodoPerFareSwap;
		else
			// modifico la coda per avere il nuovo nodo come ultimo elemento
			this.tail.next = nodoPerFareSwap;
		// inizializzo la coda con il nuovo nodo
		this.tail = nodoPerFareSwap;
		// aumento la dimensione ed il numero delle modifiche
		this.numeroModifiche++;
		this.size++;
	}

	/**
	 * Restituisce un'ArrayList contenente tutti gli hash nella lista in ordine.
	 *
	 * @return una lista con tutti gli hash della lista.
	 */
	public ArrayList<String> getAllHashes() {
		// creo l'ArrayList per contenere gli hash
		ArrayList<String> elencoDegliHash = new ArrayList<String>();
		// creo un nodo di appoggio
		Node nodoCheStoAnalizzando = this.head;
		// itero su questo nodo finchè esso non risulti nullo
		while (nodoCheStoAnalizzando != null) {
			// aggiungo questo hash alla lista e itero ancora
			elencoDegliHash.add(nodoCheStoAnalizzando.hash);
			nodoCheStoAnalizzando = nodoCheStoAnalizzando.next;
		}
		// restituisco la lista
		return elencoDegliHash;
	}

	/**
	 * Costruisce una stringa contenente tutti i nodi della lista, includendo dati e
	 * hash. La stringa dovrebbe essere formattata come nel seguente esempio:
	 * 
	 * <pre>
	 *     Dato: StringaDato1, Hash: 5d41402abc4b2a76b9719d911017c592
	 *     Dato: SteringaDato2, Hash: 7b8b965ad4bca0e41ab51de7b31363a1
	 *     ...
	 *     Dato: StringaDatoN, Hash: 2c6ee3d301aaf375b8f026980e7c7e1c
	 * </pre>
	 *
	 * @return una rappresentazione testuale di tutti i nodi nella lista.
	 */
	public String buildNodesString() {
		// Uso uno StringBuilder per accumulare le varie parti della stringa
		// formattata.
		StringBuilder stringaCostruita = new StringBuilder();
		// creo un nodo di appoggio
		Node nodoAttualeDelCiclo = this.head;
		// itero il nodo fino a quando non è nullo
		while (nodoAttualeDelCiclo != null) {
			// costruisco una stringa formattata del nodo corrente
			stringaCostruita.append("Dato: " + nodoAttualeDelCiclo.data + ", Hash: " + nodoAttualeDelCiclo.hash + "\n");
			nodoAttualeDelCiclo = nodoAttualeDelCiclo.next;
		}
		// trasformo lo StringBuilder in una stringa per fare il return
		return stringaCostruita.toString();
	}

	/**
	 * Rimuove il primo elemento nella lista che contiene il dato specificato.
	 *
	 * @param data il dato da rimuovere.
	 * @return true se l'elemento è stato trovato e rimosso, false altrimenti.
	 */
	public boolean remove(T data) {
		// controllo che la lista ed il dato non siano null
		if (this.head == null)
			return false;
		if (data == null)
			throw new IllegalArgumentException("Non puoi inserire un dato nullo");
		// controllo se l'elemento si trova nella testa O(1)
		if (this.head.data.equals(data)) {
			// rimuovo l'elemento nella testa
			this.head = this.head.next;
			// controllo se ci stava un solo elemento
			if (this.head == null)
				// se la lista contiene un solo elemento aggiorno pure la coda
				this.tail = null;
			// aumento le modifiche 
			//diminuisco la dimensione ed esco dal metodo
			this.numeroModifiche++;
			this.size--;
			return true;
		}
		// creo il nodo precedente
		Node nodoPrecedenteAlAttuale = this.head;
		// creo il nodo attuale
		Node nodoAttualmenteIterato = this.head.next;
		while (nodoAttualmenteIterato != null) {
			// controllo se il dato è quello esatto
			if (nodoAttualmenteIterato.data.equals(data)) {
				// rimuovo il nodo
				nodoPrecedenteAlAttuale.next = nodoAttualmenteIterato.next;
				// controllo se era l'ultimo
				this.tail = (nodoAttualmenteIterato.equals(this.tail)) ? nodoPrecedenteAlAttuale : this.tail;
				// aumento le modifiche e la dimensione
				this.numeroModifiche++;
				this.size--;
				return true;
			}
			// prendo il precedente ed il nodo attuale
			nodoPrecedenteAlAttuale = nodoAttualmenteIterato;
			nodoAttualmenteIterato = nodoAttualmenteIterato.next;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}

	/**
	 * Classe che realizza un iteratore fail-fast per HashLinkedList.
	 */
	private class Itr implements Iterator<T> {

		private Node nodoUltimaPosizione;
		private int conteggioModificheAspettate;

		private Itr() {
			this.nodoUltimaPosizione = null;
			this.conteggioModificheAspettate = numeroModifiche;
		}

		@Override
		public boolean hasNext() {
			// verifico se ci sono ancora elementi
			return nodoUltimaPosizione == null ? head != null : nodoUltimaPosizione.next != null;
		}

		@Override
		public T next() {
			/*
			 * Controllo che ci siano ancora elementi da iterare e controllo che non ci
			 * siano modifiche concorrenti nell'iteratore
			 *
			 */

			if (this.conteggioModificheAspettate != numeroModifiche)
				throw new ConcurrentModificationException("Errore di fail-fast");
			if (!hasNext())
				throw new NoSuchElementException("Abbiamo finito gli elementi");
			// Determino se l'iteratore è all'inizio della lista o deve avanzare al nodo
			// successivo
			nodoUltimaPosizione = (nodoUltimaPosizione == null) ? head : nodoUltimaPosizione.next;
			return nodoUltimaPosizione.data;
		}
	}
}