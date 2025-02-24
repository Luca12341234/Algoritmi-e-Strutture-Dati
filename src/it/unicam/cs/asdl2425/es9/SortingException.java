/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

/**
 * 
 * Eccezione che segnala che un algoritmo di ordinamento ha commesso un errore.
 * 
 * @author Luca Tesei
 *
 */
public class SortingException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7289728998235L;
    private final String errorDetails;
    /**
     * 
     */
    public SortingException() {
        super();
        // TODO Auto-generated constructor stub
        this.errorDetails = "Non sono stati forniti ulteriori dettagli sull'errore.";
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SortingException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
        this.errorDetails = createErrorDetail(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SortingException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
        this.errorDetails = createErrorDetail(cause);
    }

    /**
     * @param message
     */
    public SortingException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
        this.errorDetails = "Errore nell'operazione di ordinamento:" + message;
    }

    /**
     * @param cause
     */
    public SortingException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
        this.errorDetails = createErrorDetail(cause);
    }
    private String createErrorDetail(Throwable cause) {
        if (cause == null) {
            return "Nessuna causa fornita per questa eccezione di ordinamento.";
        } else {
            return "Causa: " + cause.toString();
        }
    }

}
