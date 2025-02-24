package it.unicam.cs.asdl2425.es4;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final Aula aula;

    private final TimeSlot timeSlot;

    private String docente;

    private String motivo;

    /**
     * Costruisce una prenotazione.
     * 
     * @param aula
     *                     l'aula a cui la prenotazione si riferisce
     * @param timeSlot
     *                     il time slot della prenotazione
     * @param docente
     *                     il nome del docente che ha prenotato l'aula
     * @param motivo
     *                     il motivo della prenotazione
     * @throws NullPointerException
     *                                  se uno qualsiasi degli oggetti passati è
     *                                  null
     */
    public Prenotazione(Aula aula, TimeSlot timeSlot, String docente,
            String motivo) {
    	if(aula==null || timeSlot==null || docente==null || motivo==null)
    			throw new NullPointerException();
        this.aula = aula;
        this.timeSlot = timeSlot;
        this.docente=docente;
        this.motivo=motivo;
    }

    /**
     * @return the aula
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * @return the docente
     */
    public String getDocente() {
        return docente;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param docente
     *                    the docente to set
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }

    /**
     * @param motivo
     *                   the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /*
     * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time
     * slot. Il docente e il motivo possono cambiare senza influire
     * sull'uguaglianza.
     */
    @Override
    public boolean equals(Object obj) {
    	if(obj==this) return true;
    	if(obj==null) return false;
    	if(!(obj instanceof Prenotazione)) return false;
    	Prenotazione p = (Prenotazione)obj;
    	if(!p.getAula().equals(aula)) return false;
    	if(!p.getTimeSlot().equals(timeSlot)) return false;
        return true;
    }

    /*
     * L'hashcode di una prenotazione si calcola a partire dai due campi usati
     * per equals.
     */
    @Override
    public int hashCode() {
    	final int prime = 31;
        int result = 1;
        result = prime * result + aula.hashCode();
        result = prime * result+ timeSlot.hashCode();
        return result;
    }

    /*
     * Una prenotazione precede un altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
    	if (o == null)
            throw new NullPointerException("Tentativo di confrontare con null");
        if (this.timeSlot.getStart().getTimeInMillis() < o.timeSlot.getStart().getTimeInMillis())
            return -1;
        else if (this.timeSlot.getStart().getTimeInMillis() > o.timeSlot.getStart().getTimeInMillis())
            return 1;
        if (this.timeSlot.getStop().getTimeInMillis() < o.timeSlot.getStop().getTimeInMillis())
            return -1;
        else if (this.timeSlot.getStop().getTimeInMillis() > o.timeSlot.getStop().getTimeInMillis())
            return 1;
        return this.aula.compareTo(o.aula);
    }

    @Override
    public String toString() {
        return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot
                + ", docente=" + docente + ", motivo=" + motivo + "]";
    }

}
