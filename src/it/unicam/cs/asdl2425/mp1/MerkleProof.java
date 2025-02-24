package it.unicam.cs.asdl2425.mp1;

/**
 * Una classe che rappresenta una prova di Merkle per un determinato albero di
 * Merkle ed un suo elemento o branch. Oggetti di questa classe rappresentano un
 * proccesso di verifica auto-contenuto, dato da una sequenza di oggetti
 * MerkleProofHash che rappresentano i passaggi necessari per validare un dato
 * elemento o branch in un albero di Merkle decisi al momento di costruzione
 * della prova.
 * 
 * @author Luca Tesei, Marco Caputo (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 * 
 */
public class MerkleProof {

	/**
	 * La prova di Merkle, rappresentata come una lista concatenata di oggetti
	 * MerkleProofHash.
	 */
	private final HashLinkedList<MerkleProofHash> proof;

	/**
	 * L'hash della radice dell'albero di Merkle per il quale la prova è stata
	 * costruita.
	 */
	private final String rootHash;

	/**
	 * Lunghezza massima della prova, dato dal numero di hash che la compongono
	 * quando completa. Serve ad evitare che la prova venga modificata una volta che
	 * essa sia stata completamente costruita.
	 */
	private final int length;

	/**
	 * Costruisce una nuova prova di Merkle per un dato albero di Merkle,
	 * specificando la radice dell'albero e la lunghezza massima della prova. La
	 * lunghezza massima della prova è il numero di hash che la compongono quando
	 * completa, oltre il quale non è possibile aggiungere altri hash.
	 *
	 * @param rootHash l'hash della radice dell'albero di Merkle.
	 * @param length   la lunghezza massima della prova.
	 */
	public MerkleProof(String rootHash, int length) {
		if (rootHash == null)
			throw new IllegalArgumentException("The root hash is null");
		this.proof = new HashLinkedList<>();
		this.rootHash = rootHash;
		this.length = length;
	}

	/**
	 * Restituisce la massima lunghezza della prova, dato dal numero di hash che la
	 * compongono quando completa.
	 *
	 * @return la massima lunghezza della prova.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Aggiunge un hash alla prova di Merkle, specificando se esso dovrebbe essere
	 * concatenato a sinistra o a destra durante la verifica della prova. Se la
	 * prova è già completa, ovvero ha già raggiunto il massimo numero di hash
	 * deciso alla sua costruzione, l'hash non viene aggiunto e la funzione
	 * restituisce false.
	 *
	 * @param hash   l'hash da aggiungere alla prova.
	 * @param isLeft true se l'hash dovrebbe essere concatenato a sinistra, false
	 *               altrimenti.
	 * @return true se l'hash è stato aggiunto con successo, false altrimenti.
	 */
	public boolean addHash(String hash, boolean isLeft) {
		// Controllo se la prova di Merkle (lista concatenata) ha spazio per ulteriori
		// hash.
		if (this.proof.getSize() >= this.getLength())
			return false;
		// se ha spazio allora inserisco l'hash in coda
		this.proof.addAtTail(new MerkleProofHash(hash, isLeft));
		return true;
	}

	/**
	 * Rappresenta un singolo step di una prova di Merkle per la validazione di un
	 * dato elemento.
	 */
	public static class MerkleProofHash {
		/**
		 * L'hash dell'oggetto.
		 */
		private final String hash;

		/**
		 * Indica se l'hash dell'oggetto dovrebbe essere concatenato a sinistra durante
		 * la verifica della prova.
		 */
		private final boolean isLeft;

		public MerkleProofHash(String hash, boolean isLeft) {
			if (hash == null)
				throw new IllegalArgumentException("The hash cannot be null");

			this.hash = hash;
			this.isLeft = isLeft;
		}

		/**
		 * Restituisce l'hash dell'oggetto MerkleProofHash.
		 *
		 * @return l'hash dell'oggetto MerkleProofHash.
		 */
		public String getHash() {
			return hash;
		}

		/**
		 * Restituisce true se, durante la verifica della prova, l'hash dell'oggetto
		 * dovrebbe essere concatenato a sinistra, false altrimenti.
		 *
		 * @return true se l'hash dell'oggetto dovrebbe essere concatenato a sinistra,
		 *         false altrimenti.
		 */
		public boolean isLeft() {
			return isLeft;
		}

		@Override
		public boolean equals(Object obj) {
			/*
			 * Due MerkleProofHash sono uguali se hanno lo stesso hash e lo stesso flag
			 * isLeft
			 */
			// controllo se i due riferimenti puntano allo stesso Object
			if (this == obj)
				return true;
			// se non hanno la stessa istanza allora restituisco false
			if (!(obj instanceof MerkleProofHash))
				return false;
			// se il controllo di prima va a buon fine allora faccio il casting
			MerkleProofHash other = (MerkleProofHash) obj;
			// controllo se sono uguali tutti e due i parametri
			return this.hash.equals(other.getHash()) && this.isLeft() == other.isLeft();
		}

		@Override
		public String toString() {
			return hash + (isLeft ? "L" : "R");
		}

		@Override
		public int hashCode() {
			/*
			 * Implementare in accordo a equals
			 */
			// faccio l'hashcode con la versione toString che contiene
			// le stesse variabili in accordo con equals
			final int prime = 31;
			int result = 1;
			long temp;
			temp = (this.hash == null) ? 0L : (long) this.toString().hashCode();
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;

		}
	}

	/**
	 * Valida un dato elemento per questa prova di Merkle. La verifica avviene
	 * combinando l'hash del dato con l'hash del primo oggetto MerkleProofHash in un
	 * nuovo hash, il risultato con il successivo e così via fino all'ultimo
	 * oggetto, e controllando che l'hash finale coincida con quello del nodo radice
	 * dell'albero di Merkle orginale.
	 *
	 * @param data l'elemento da validare.
	 * @return true se il dato è valido secondo la prova; false altrimenti.
	 * @throws IllegalArgumentException se il dato è null.
	 */
	public boolean proveValidityOfData(Object data) {
		// Mi assicuro che il dato non sia null
		if (data == null)
			throw new IllegalArgumentException("data nullo");
		// richiamo il metodo privato validityIterator
		String risultatoCatenaHash = this.validityIterator(HashUtil.dataToHash(data));
		// controllo se l'hash finale è uguale a quello calcolato
		return (this.rootHash.equals(risultatoCatenaHash));
	}

	/**
	 * Valida un dato branch per questa prova di Merkle. La verifica avviene
	 * combinando l'hash del branch con l'hash del primo oggetto MerkleProofHash in
	 * un nuovo hash, il risultato con il successivo e così via fino all'ultimo
	 * oggetto, e controllando che l'hash finale coincida con quello del nodo radice
	 * dell'albero di Merkle orginale.
	 *
	 * @param branch il branch da validare.
	 * @return true se il branch è valido secondo la prova; false altrimenti.
	 * @throws IllegalArgumentException se il branch è null.
	 */
	public boolean proveValidityOfBranch(MerkleNode branch) {
		// Mi assicuro che il branch non sia null
		if (branch == null)
			throw new IllegalArgumentException("branch nullo");
		// richiamo il metodo privato validityIterator
		String risultatoCatenaHash = this.validityIterator(branch.getHash());
		// controllo se l'hash finale è uguale a quello calcolato
		return (this.rootHash.equals(risultatoCatenaHash));

	}

	// inseriti metodi privati per fini di implementazione

	/**
	 * Itera sulla hashlinkedlist proof e calcola l'hash per il controllo con la root
	 * 
	 * @param hash iniziale
	 * @return hash da controllare con la root
	 */
	private String validityIterator(String initialHash) {
		// scorro tutta la HashLinkedList prof
		for (MerkleProofHash hashNodoCorrente : this.proof) {
			// controllo se deve essere messa a sinistra l'hash corrente
			initialHash = hashNodoCorrente.isLeft()
					? HashUtil.computeMD5((hashNodoCorrente.getHash() + initialHash).getBytes())
					: HashUtil.computeMD5((initialHash + hashNodoCorrente.getHash()).getBytes());
		}
		return initialHash;
	}

}
