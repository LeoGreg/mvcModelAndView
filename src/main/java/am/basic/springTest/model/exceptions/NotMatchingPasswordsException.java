package am.basic.springTest.model.exceptions;

public class NotMatchingPasswordsException extends Exception {

    public NotMatchingPasswordsException(String message) {
        super(message);
    }


    public static void check(boolean expresion, String message) throws NotMatchingPasswordsException {
        if (expresion) {
            throw new NotMatchingPasswordsException(message);
        }
    }

}
