package it.unicam.cs.asdl2425.es2;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 */
public class CombinationLock {
	private String aCombination;
	private char pos[];
	private int i;
	private boolean open;
	private final static String comb = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Costruisce una cassaforte <b>aperta</b> con una data combinazione
	 * 
	 * @param aCombination la combinazione che deve essere una stringa di 3 lettere
	 *                     maiuscole dell'alfabeto inglese
	 * @throw IllegalArgumentException se la combinazione fornita non è una stringa
	 *        di 3 lettere maiuscole dell'alfabeto inglese
	 * @throw NullPointerException se la combinazione fornita è nulla
	 */
	public CombinationLock(String aCombination) {
		if (aCombination == null)
			throw new NullPointerException("Errore");
		if (aCombination.length() != 3 || !comb.contains("" + aCombination.charAt(0))
				|| !comb.contains("" + aCombination.charAt(1)) ||
				!comb.contains("" + aCombination.charAt(2)))
			throw new IllegalArgumentException("Errore");
		this.aCombination = aCombination;
		this.pos = new char[3];
		this.open = true;
		this.i = 0;
	}

	/**
	 * Imposta la manopola su una certaposizione.
	 * 
	 * @param aPosition un carattere lettera maiuscola su cui viene impostata la
	 *                  manopola
	 * @throws IllegalArgumentException se il carattere fornito non è una lettera
	 *                                  maiuscola dell'alfabeto inglese
	 */
	public void setPosition(char aPosition) {
		if (!comb.contains("" + aPosition))
			throw new IllegalArgumentException("Errore");
		if (i == 3)
			i = 0;
		this.pos[i++] = aPosition;
	}

	/**
	 * Tenta di aprire la serratura considerando come combinazione fornita le ultime
	 * tre posizioni impostate. Se l'apertura non va a buon fine le lettere
	 * impostate precedentemente non devono essere considerate per i prossimi
	 * tentativi di apertura.
	 */
	public void open() {
		if (i == 3 && pos[0] == aCombination.charAt(0) && pos[1] == aCombination.charAt(1)
				&& pos[2] == aCombination.charAt(2))
			open = true;
		else {
			this.pos = new char[3];
			this.i = 0;
		}
	}

	/**
	 * Determina se la cassaforte è aperta.
	 * 
	 * @return true se la cassaforte è attualmente aperta, false altrimenti
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo che
	 * se si prova a riaprire subito senza impostare nessuna nuova posizione della
	 * manopola la cassaforte non si apre. Si noti che se la cassaforte era stata
	 * aperta con la combinazione giusta le ultime posizioni impostate sono proprio
	 * la combinazione attuale.
	 */
	public void lock() {
		if (open == true) {
			this.open = false;
			this.pos = new char[3];
			this.i = 0;
		}
	}

	/**
	 * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
	 * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa rimane
	 * chiusa e la combinazione non viene cambiata, ma in questo caso le le lettere
	 * impostate precedentemente non devono essere considerate per i prossimi
	 * tentativi di apertura.
	 * 
	 * @param aCombination la nuova combinazione che deve essere una stringa di 3
	 *                     lettere maiuscole dell'alfabeto inglese
	 * @throw IllegalArgumentException se la combinazione fornita non è una stringa
	 *        di 3 lettere maiuscole dell'alfabeto inglese
	 * @throw NullPointerException se la combinazione fornita è nulla
	 */
	public void lockAndChangeCombination(String aCombination) {
		if (open == true) {
			if (aCombination == null)
				throw new NullPointerException("Errore");
			if (aCombination.length() != 3 || !comb.contains("" + aCombination.charAt(0))
					|| !comb.contains("" + aCombination.charAt(1)) || !comb.contains("" + aCombination.charAt(2)))
				throw new IllegalArgumentException("Errore");
			this.open = false;
			this.aCombination = aCombination;
			this.pos = new char[3];
			this.i = 0;
		} else {
			this.pos = new char[3];
			this.i = 0;
		}
	}
}