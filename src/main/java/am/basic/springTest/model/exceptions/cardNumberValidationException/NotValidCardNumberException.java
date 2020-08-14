package am.basic.springTest.model.exceptions.cardNumberValidationException;


public class NotValidCardNumberException extends Exception {
    public NotValidCardNumberException(String message) {
        super(message);
    }


    public static void isNotValid(boolean expresion, String message) throws NotValidCardNumberException {
        if (expresion) {
            throw new NotValidCardNumberException(message);
        }
    }
}
