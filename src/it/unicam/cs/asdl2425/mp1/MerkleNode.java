package it.unicam.cs.asdl2425.mp1;

/**
 * Rappresenta un nodo di un albero di Merkle.
 * 
 * @author Luca Tesei, Marco Caputo (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 * 
 */
public class MerkleNode {
	private final String hash; // Hash associato al nodo.

	private final MerkleNode left; // Figlio sinistro del nodo.

	private final MerkleNode right; // Figlio destro del nodo.

	/**
	 * Costruisce un nodo Merkle foglia con un valore di hash, quindi,
	 * corrispondente all'hash di un dato.
	 *
	 * @param hash l'hash associato al nodo.
	 */
	public MerkleNode(String hash) {
		this(hash, null, null);
	}

	/**
	 * Costruisce un nodo Merkle con un valore di hash e due figli, quindi,
	 * corrispondente all'hash di un branch.
	 *
	 * @param hash  l'hash associato al nodo.
	 * @param left  il figlio sinistro.
	 * @param right il figlio destro.
	 */
	public MerkleNode(String hash, MerkleNode left, MerkleNode right) {
		this.hash = hash;
		this.left = left;
		this.right = right;
	}

	/**
	 * Restituisce l'hash associato al nodo.
	 *
	 * @return l'hash associato al nodo.
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Restituisce il figlio sinistro del nodo.
	 *
	 * @return il figlio sinistro del nodo.
	 */
	public MerkleNode getLeft() {
		return left;
	}

	/**
	 * Restituisce il figlio destro del nodo.
	 *
	 * @return il figlio destro del nodo.
	 */
	public MerkleNode getRight() {
		return right;
	}

	/**
	 * Restituisce true se il nodo è una foglia, false altrimenti.
	 *
	 * @return true se il nodo è una foglia, false altrimenti.
	 */
	public boolean isLeaf() {
		// il nodo è una foglia se non ha il figlio sinistro ed il figlio destro
		if (this.left == null && this.right == null)
			return true;
		// se invece ha un figlio sinistro o destro oppure entrambi i figli allora non è una foglia
		return false;
	}

	@Override
	public String toString() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		/* due nodi sono uguali se hanno lo stesso hash */
		// controllo il riferimento
		if (this == obj)
			return true;
		// controllo se obj è istanza di MerkleNode
		if (!(obj instanceof MerkleNode))
			return false;
		// se il controllo di prima va a buon fine allora faccio il casting
		MerkleNode otherMerkleNode = (MerkleNode) obj;
		// controllo l'uguaglianza
		return this.hash.equals(otherMerkleNode.getHash());

	}

	@Override
	public int hashCode() {
		/* implementare in accordo a equals */
		// faccio l'hashcode della stringa hash
		final int prime = 31;
		int result = 1;
		long temp;
		temp = (this.hash == null) ? 0L : (long) hash.hashCode();
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;

	}
}