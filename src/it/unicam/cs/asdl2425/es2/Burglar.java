package it.unicam.cs.asdl2425.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

    
	private CombinationLock aCombinationLock;
	private int attempt;
    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
    	if(aCombinationLock==null) throw new NullPointerException();
    	this.aCombinationLock=aCombinationLock;
    	this.attempt=-1;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
    	for (char c = 'A'; c <= 'Z'; c++) {
    		for (char cccc = 'A'; cccc <= 'Z'; cccc++) {
    			for (char ccc = 'A'; ccc <= 'Z'; ccc++) {
    				attempt++;
    				aCombinationLock.setPosition(c);
    				aCombinationLock.setPosition(cccc);
    				aCombinationLock.setPosition(ccc);
    				aCombinationLock.open();
    				if(aCombinationLock.isOpen()) {
    					char []array = {c,cccc,ccc};
    					String str= new String(array);
    					return str;
    				}
    	    	}
        	}
    	}
    	return null;
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        return attempt;
    }
}
