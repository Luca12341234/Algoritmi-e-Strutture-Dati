package it.unicam.cs.asdl2425.mp1;

import java.util.*;

/**
 * Un Merkle Tree, noto anche come hash tree binario, è una struttura dati per
 * verificare in modo efficiente l'integrità e l'autenticità dei dati
 * all'interno di un set di dati più ampio. Viene costruito eseguendo l'hashing
 * ricorsivo di coppie di dati (valori hash crittografici) fino a ottenere un
 * singolo hash root. In questa implementazione la verifica di dati avviene
 * utilizzando hash MD5.
 * 
 * @author Luca Tesei, Marco Caputo (template), Luca Magrini luca.magrini@studenti.unicam.it (implementazione)
 *
 * @param <T> il tipo di dati su cui l'albero è costruito.
 */
public class MerkleTree<T> {
	/**
	 * Nodo radice dell'albero.
	 */
	private final MerkleNode root;

	/**
	 * Larghezza dell'albero, ovvero il numero di nodi nell'ultimo livello.
	 */
	private final int width;

	/**
	 * Costruisce un albero di Merkle a partire da un oggetto HashLinkedList,
	 * utilizzando direttamente gli hash presenti nella lista per costruire le
	 * foglie. Si noti che gli hash dei nodi intermedi dovrebbero essere ottenuti da
	 * quelli inferiori concatenando hash adiacenti due a due e applicando
	 * direttmaente la funzione di hash MD5 al risultato della concatenazione in
	 * bytes.
	 *
	 * @param hashList un oggetto HashLinkedList contenente i dati e i relativi
	 *                 hash.
	 * @throws IllegalArgumentException se la lista è null o vuota.
	 */
	public MerkleTree(HashLinkedList<T> hashList) {
		// controllo che la lista non è null e abbia almeno un elemento
		if (hashList == null || hashList.getSize() == 0)
			throw new IllegalArgumentException("lista null oppure vuota");

		// creo una List per contenere i nodi foglia
		List<MerkleNode> nodiFoglia = new ArrayList<MerkleNode>();
		// creo i nodi foglia da mettere dentro l'ArrayList nodiFoglia
		for (String hashSingolo : hashList.getAllHashes())
			nodiFoglia.add(new MerkleNode(hashSingolo));

		// costruisco l'albero
		this.root = this.creatoreDellMerkle(nodiFoglia);
		// gli passo la larghezza (numero nodi foglia)
		this.width = hashList.getSize();
	}

	/**
	 * Restituisce il nodo radice dell'albero.
	 *
	 * @return il nodo radice.
	 */
	public MerkleNode getRoot() {
		return root;
	}

	/**
	 * Restituisce la larghezza dell'albero.
	 *
	 * @return la larghezza dell'albero.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Restituisce l'altezza dell'albero.
	 *
	 * @return l'altezza dell'albero.
	 */
	public int getHeight() {
		// è un albero a cui si può applicare log2(n)
		return (int) Math.ceil((Math.log(this.getWidth()) / Math.log(2)));
	}

	/**
	 * Restituisce l'indice di un dato elemento secondo l'albero di Merkle descritto
	 * da un dato branch. Gli indici forniti partono da 0 e corrispondono all'ordine
	 * degli hash corrispondenti agli elementi nell'ultimo livello dell'albero da
	 * sinistra a destra. Nel caso in cui il branch fornito corrisponda alla radice
	 * di un sottoalbero, l'indice fornito rappresenta un indice relativo a quel
	 * sottoalbero, ovvero un offset rispetto all'indice del primo elemento del
	 * blocco di dati che rappresenta. Se l'hash dell'elemento non è presente come
	 * dato dell'albero, viene restituito -1.
	 *
	 * @param branch la radice dell'albero di Merkle.
	 * @param data   l'elemento da cercare.
	 * @return l'indice del dato nell'albero; -1 se l'hash del dato non è presente.
	 * @throws IllegalArgumentException se il branch o il dato sono null o se il
	 *                                  branch non è parte dell'albero.
	 */
	public int getIndexOfData(MerkleNode branch, T data) {
		// controllo che il branch non sia null o che non lo sia il data oppure
		// che il branch sia valido
		if (branch == null || data == null || !this.validateBranch(branch))
			throw new IllegalArgumentException("brach null o data null oppure branch non è parte dell'albero");
		// calcolo l'hash del data passato come parametro
		String hashDelData = HashUtil.dataToHash(data);
		// restutisco l'indice della foglia mettendo true come parametro
		// in modo tale che mi consideri solo gli indici delle foglie
		return this.indiciFogliaOrIntermedi(branch, hashDelData, 0, true);
	}

	/**
	 * Restituisce l'indice di un elemento secondo questo albero di Merkle. Gli
	 * indici forniti partono da 0 e corrispondono all'ordine degli hash
	 * corrispondenti agli elementi nell'ultimo livello dell'albero da sinistra a
	 * destra (e quindi l'ordine degli elementi forniti alla costruzione). Se l'hash
	 * dell'elemento non è presente come dato dell'albero, viene restituito -1.
	 *
	 * @param data l'elemento da cercare.
	 * @return l'indice del dato nell'albero; -1 se il dato non è presente.
	 * @throws IllegalArgumentException se il dato è null.
	 */
	public int getIndexOfData(T data) {
		// controllo che il data non sia null
		if (data == null)
			throw new IllegalArgumentException("data null");
		// calcolo l'hash del data passato come parametro
		String hashDelData = HashUtil.dataToHash(data);
		// se questo hash è uguale a un hash dei nodi foglia allora
		// restituisco l'indice, -1 altrimenti.
		return this.indiciFogliaOrIntermedi(this.getRoot(), hashDelData, 0, true);
	}

	/**
	 * Sottopone a validazione un elemento fornito per verificare se appartiene
	 * all'albero di Merkle, controllando se il suo hash è parte dell'albero come
	 * hash di un nodo foglia.
	 *
	 * @param data l'elemento da validare
	 * @return true se l'hash dell'elemento è parte dell'albero; false altrimenti.
	 */
	public boolean validateData(T data) {
		// controllo se il data ha un indice e se lo ha allora è valido
		return this.getIndexOfData(data) != -1;
	}

	/**
	 * Sottopone a validazione un dato sottoalbero di Merkle, corrispondente quindi
	 * a un blocco di dati, per verificare se è valido rispetto a questo albero e ai
	 * suoi hash. Un sottoalbero è valido se l'hash della sua radice è uguale
	 * all'hash di un qualsiasi nodo intermedio di questo albero. Si noti che il
	 * sottoalbero fornito può corrispondere a una foglia.
	 *
	 * @param branch la radice del sottoalbero di Merkle da validare.
	 * @return true se il sottoalbero di Merkle è valido; false altrimenti.
	 */
	public boolean validateBranch(MerkleNode branch) {
		// controllo che il branch non sia null
		if (branch == null)
			throw new IllegalArgumentException("branch null");
		// prendo l'hash del nodo passato
		String hashBranchDellaRoot = branch.getHash();
		// metto false sulla funzione indiciFogliaOrIntermedi per includere tutti i nodi
		// anche quelli intermedi e poi se l'indice è diverso da - 1 allora confermo
		// che il branch è valido
		return this.indiciFogliaOrIntermedi(this.getRoot(), hashBranchDellaRoot, 0, false) != -1;
	}

	/**
	 * Sottopone a validazione un dato albero di Merkle per verificare se è valido
	 * rispetto a questo albero e ai suoi hash. Grazie alle proprietà degli alberi
	 * di Merkle, ciò può essere fatto in tempo costante.
	 *
	 * @param otherTree il nodo radice dell'altro albero di Merkle da validare.
	 * @return true se l'altro albero di Merkle è valido; false altrimenti.
	 * @throws IllegalArgumentException se l'albero fornito è null.
	 */
	public boolean validateTree(MerkleTree<T> otherTree) {
		// controllo che l'albero fornito non sia null
		if (otherTree == null)
			throw new IllegalArgumentException("otherTree null");
		// prendo le radici di questo albero e quelle dell'albero passato come parametro
		String hashBranchDellaRootCorrente = this.getRoot().getHash();
		String hashBranchDellaRootNonCorrente = otherTree.getRoot().getHash();
		// se hanno la stessa radice allora l'albero passato è valido
		// questo è fatto in un tempo costante O(1)
		return hashBranchDellaRootCorrente.equals(hashBranchDellaRootNonCorrente);
	}

	/**
	 * Trova gli indici degli elementi di dati non validi (cioè con un hash diverso)
	 * in un dato Merkle Tree, secondo questo Merkle Tree. Grazie alle proprietà
	 * degli alberi di Merkle, ciò può essere fatto confrontando gli hash dei nodi
	 * interni corrispondenti nei due alberi. Ad esempio, nel caso di un singolo
	 * dato non valido, verrebbe percorso un unico cammino di lunghezza pari
	 * all'altezza dell'albero. Gli indici forniti partono da 0 e corrispondono
	 * all'ordine degli elementi nell'ultimo livello dell'albero da sinistra a
	 * destra (e quindi l'ordine degli elementi forniti alla costruzione). Se
	 * l'albero fornito ha una struttura diversa, possibilmente a causa di una
	 * quantità diversa di elementi con cui è stato costruito e, quindi, non
	 * rappresenta gli stessi dati, viene lanciata un'eccezione.
	 *
	 * @param otherTree l'altro Merkle Tree.
	 * @throws IllegalArgumentException se l'altro albero è null o ha una struttura
	 *                                  diversa.
	 * @return l'insieme di indici degli elementi di dati non validi.
	 */
	public Set<Integer> findInvalidDataIndices(MerkleTree<T> otherTree) {
		// controllo che l'albero abbia la stessa struttura (larghezza)
		// e che non sia null
		if (otherTree == null || this.getWidth() != otherTree.getWidth())
			throw new IllegalArgumentException("albero passato null o con una struttura diversa");
		// prendo un set dove metterò gli indici non validi
		Set<Integer> indiciNonValidi = new HashSet<Integer>();
		// richiamo la funzione che cercherà in maniera ricorsiva gli indici non validi
		this.verificaDatiInvalidiNodo(this.getRoot(), otherTree.getRoot(), 0, indiciNonValidi);
		// restituisco il set degli indici
		return indiciNonValidi;
	}

	/**
	 * Restituisce la prova di Merkle per un dato elemento, ovvero la lista di hash
	 * dei nodi fratelli di ciascun nodo nel cammino dalla radice a una foglia
	 * contenente il dato. La prova di Merkle dovrebbe fornire una lista di oggetti
	 * MerkleProofHash tale per cui, combinando l'hash del dato con l'hash del primo
	 * oggetto MerkleProofHash in un nuovo hash, il risultato con il successivo e
	 * così via fino all'ultimo oggetto, si possa ottenere l'hash del nodo padre
	 * dell'albero. Nel caso in cui non ci, in determinati step della prova non ci
	 * siano due hash distinti da combinare, l'hash deve comunque ricalcolato sulla
	 * base dell'unico hash disponibile.
	 *
	 * @param data l'elemento per cui generare la prova di Merkle.
	 * @return la prova di Merkle per il dato.
	 * @throws IllegalArgumentException se il dato è null o non è parte dell'albero.
	 */
	public MerkleProof getMerkleProof(T data) {
		// controllo che il dato non sia null e che faccia parte dell'albero
		if (data == null || !this.validateData(data))
			throw new IllegalArgumentException("dato null o non parte dell'albero");
		// richiamo la funzione getMerkleProof
		return this.getMerkleProof(new MerkleNode(HashUtil.dataToHash(data)));
	}

	/**
	 * Restituisce la prova di Merkle per un dato branch, ovvero la lista di hash
	 * dei nodi fratelli di ciascun nodo nel cammino dalla radice al dato nodo
	 * branch, rappresentativo di un blocco di dati. La prova di Merkle dovrebbe
	 * fornire una lista di oggetti MerkleProofHash tale per cui, combinando l'hash
	 * del branch con l'hash del primo oggetto MerkleProofHash in un nuovo hash, il
	 * risultato con il successivo e così via fino all'ultimo oggetto, si possa
	 * ottenere l'hash del nodo padre dell'albero. Nel caso in cui non ci, in
	 * determinati step della prova non ci siano due hash distinti da combinare,
	 * l'hash deve comunque ricalcolato sulla base dell'unico hash disponibile.
	 *
	 * @param branch il branch per cui generare la prova di Merkle.
	 * @return la prova di Merkle per il branch.
	 * @throws IllegalArgumentException se il branch è null o non è parte
	 *                                  dell'albero.
	 */
	public MerkleProof getMerkleProof(MerkleNode branch) {
		// controllo che il branch non sia null o parte dell'albero
		// nb: questo si può fare perchè uso || e non | come or
		if (branch == null || !this.validateBranch(branch)) {
			throw new IllegalArgumentException("branch nullo oppure non parte dell'albero");
		}
		// prendo il percorso dalla radice fino al nodo con quell'hash
		List<MerkleNode> sequenzaNodiPercorso = scopriPercorsoNodo(this.getRoot(), branch.getHash());
		if (sequenzaNodiPercorso == null)
			throw new IllegalArgumentException("percorso nullo");
		// creo una nuova prova di merkle
		MerkleProof provaMerkleGenerata = new MerkleProof(this.getRoot().getHash(), sequenzaNodiPercorso.size() - 1);
		// faccio un for decrementale in modo tale da partire dal basso verso l'alto
		for (int i = sequenzaNodiPercorso.size() - 1; i > 0; i--) {
			// variabili di memorizzazione del nodo fratello e della flag per l'aggiunta
			// in provaMerkleGenerata tramite il metodo addHash
			MerkleNode nodoFratello = null;
			boolean isLeft = false;
			// se il nodo radice ha come nodo sinistro il nodo i allora...
			if (sequenzaNodiPercorso.get(i - 1).getLeft().equals(sequenzaNodiPercorso.get(i))) {
				// ...se sono uguali allora prendo il fratello verso destra e poi isLeft rimane
				// false
				nodoFratello = sequenzaNodiPercorso.get(i - 1).getRight();
			} else {
				// ...se sono diversi allora prendo il fratello verso sinistra poi isLeft deve
				// essere true
				nodoFratello = sequenzaNodiPercorso.get(i - 1).getLeft();
				isLeft = true;
			}

			// aggiungo un hash alla prova di Merkle dove metto l'hash del nodo fratello
			// se esso non è null, altrimenti aggiungo "" come hash come si intravede dai
			// test
			provaMerkleGenerata.addHash((nodoFratello != null) ? nodoFratello.getHash() : "", isLeft);
		}
		// ritorno la prova di merkle
		return provaMerkleGenerata;
	}

	// inseriti metodi privati per fini di implementazione

	/**
	 * Restituisce una root che è collegata a dei nodi figli e così via partendo da
	 * una lista iniziale per poi ricreare il MerkleTree e quindi creo un nuovo
	 * livello ogni volta e combino i nodi foglia del livello precedente.
	 *
	 * @param List dei nodi di un livello
	 * @return root del merkle tree
	 */
	private MerkleNode creatoreDellMerkle(List<MerkleNode> nodes) {
		// se la dimensione della lista è 1 allora contiene la root e quindi la
		// restituisco
		if (nodes.size() == 1)
			return nodes.get(0);
		// se non lo è allora creo un nuovo Arraylist di appoggio dove metterò i nuovi
		// nodi
		List<MerkleNode> serieNodiGenitori = new ArrayList<MerkleNode>();
		for (int i = 0; i < nodes.size(); i += 2) {
			// il nodo sinistro ce lo ha sempre sia che la lista abbia
			// dimensione pari o dispari
			MerkleNode nodoSX = nodes.get(i);
			// creo delle variabili di appoggio per l'hash e per il nodo destro
			MerkleNode nodoDX = null;
			String hashDellaRoot = null;
			// caso 1: non siamo alla fine oppure siamo alla fine ma la lista
			// ha una dimensione pari
			if (i + 1 < nodes.size()) {
				nodoDX = nodes.get(i + 1);
				hashDellaRoot = HashUtil.computeMD5((nodoSX.getHash() + nodoDX.getHash()).getBytes());
			} else {
				// caso2: siamo alla fine dell'iterazione in una lista con dimensione non pari
				hashDellaRoot = HashUtil.computeMD5((nodoSX.getHash()).getBytes());
			}
			// aggiungo alla List un nuovo nodo con i riferimenti precedenti
			serieNodiGenitori.add(new MerkleNode(hashDellaRoot, nodoSX, nodoDX));
		}
		// restituisco il nuovo livello e faccio la ricorsione
		return this.creatoreDellMerkle(serieNodiGenitori);
	}

	/**
	 * Restituisce l'indice della foglia interessata oppure l'indice dei nodi di
	 * quel livello di cui siamo interessati e questo lo fa moltiplicando l'indice*2
	 * a sinistra e poi moltiplicando l'indice*2+1 a destra.
	 *
	 * @param root del sotto albero, hash da controllare, indice
	 * @return indice del nodo nel livello intermedio o ultimo
	 */

	private int indiciFogliaOrIntermedi(MerkleNode branch, String data, int indice, boolean foglie) {
		// se sono arrivato alla fine e non ho trovato niente come indice allora
		// restituisco -1
		if (branch == null)
			return -1;
		// se non devo trovare SOLO gli indici foglia e gli hash sono uguali allora
		// restituisco l'indice di quel livello
		if (!foglie && branch.getHash().equals(data))
			return indice;

		// se è una foglia e ha un hash uguale allora restituisco l'indice dell'ultimo
		// livello.
		if (branch.isLeaf())
			return branch.getHash().equals(data) ? indice : -1;
		// se non trovo l'hash allora faccio la ricorsione sulla parte sinistra
		// moltiplicando l'indice per 2
		int risultatoSx = this.indiciFogliaOrIntermedi(branch.getLeft(), data, indice * 2, foglie);
		if (risultatoSx != -1)
			return risultatoSx;

		// se non trovo l'hash neanche sulla parte sinistra allora faccio la ricorsione
		// sulla parte destra moltiplicando l'indice per 2 e aggiungendo + 1
		int risultatoDx = this.indiciFogliaOrIntermedi(branch.getRight(), data, indice * 2 + 1, foglie);
		return risultatoDx;
	}

	/**
	 * Metodo usato per trovare gli indici che non sono validi dei nodi foglia
	 *
	 *
	 * @param root di questo albero, root dell'altro albero da confrontare, indice
	 *             dei nodi, set di nodi invalidi
	 */
	private void verificaDatiInvalidiNodo(MerkleNode node, MerkleNode otherNode, int nodesOnLeft,
			Set<Integer> invalidIndices) {
		// se il nodo di uno dei due alberi è null allora esco dal metodo
		if (node == null || otherNode == null)
			return;
		// se gli hash sono diversi allora c'è qualcosa di invalido e quindi...
		if (!node.getHash().equals(otherNode.getHash())) {
			// ...se è una foglia allora aggiungo il suo indice agli altri indici...
			if (node.isLeaf()) {
				invalidIndices.add(nodesOnLeft);
				return;
			}
			// ...altrimenti rifaccio la ricorsione finchè non riesco ad arrivare ad una foglia
			this.verificaDatiInvalidiNodo(node.getLeft(), otherNode.getLeft(), nodesOnLeft * 2, invalidIndices);
			this.verificaDatiInvalidiNodo(node.getRight(), otherNode.getRight(), nodesOnLeft * 2 + 1, invalidIndices);
		}
	}

	/**
	 * Metodo usato per trovare il percorso dalla root alla foglia
	 *
	 * @param nodo corrente ed hash da controllare
	 * @return list dei nodi del percorso interessato
	 */
	private List<MerkleNode> scopriPercorsoNodo(MerkleNode currentNode, String dataHash) {
		// se il nodo corrente è nullo allora ritorno null
		if (currentNode == null)
			return null;
		// se il nodo corrente ha l'hash uguale all'hash da verificare allora lo aggiungo
		// alla lista e restituisco la lista
		if (currentNode.getHash().equals(dataHash)) {
			List<MerkleNode> percorso = new ArrayList<MerkleNode>();
			percorso.add(currentNode);
			return percorso;
		}
		// faccio la ricorsione sul nodo sinistro
		List<MerkleNode> percorso = this.scopriPercorsoNodo(currentNode.getLeft(), dataHash);
		// se il path sul nodo sinsitro porta null allora provo il sottoalbero destro
		if (percorso == null)
			percorso = this.scopriPercorsoNodo(currentNode.getRight(), dataHash);

		// se una delle due ricorsioni ha portato risulatti metto il nodo nel primo indice tramite
		// add(indice,elem);
		if (percorso != null)
			percorso.add(0, currentNode);
		// ritorno null o il path
		return percorso;
	}
}