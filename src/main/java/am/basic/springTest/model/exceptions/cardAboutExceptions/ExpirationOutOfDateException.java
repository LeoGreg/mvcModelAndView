package am.basic.springTest.model.exceptions.cardAboutExceptions;

public class ExpirationOutOfDateException extends Exception {
    public ExpirationOutOfDateException(String message) {
        super(message);
    }


    public static void isThroughDate(boolean expresion, String message) throws ExpirationOutOfDateException {
        if (expresion) {
            throw new ExpirationOutOfDateException(message);
        }
    }
}
