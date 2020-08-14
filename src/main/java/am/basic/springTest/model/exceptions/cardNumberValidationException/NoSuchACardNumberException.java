package am.basic.springTest.model.exceptions.cardNumberValidationException;

public class NoSuchACardNumberException extends Exception {
    public NoSuchACardNumberException(String message) {
        super(message);
    }


    public static void check(boolean expresion, String message) throws NoSuchACardNumberException {
        if (expresion) {
            throw new NoSuchACardNumberException(message);
        }
    }
}
