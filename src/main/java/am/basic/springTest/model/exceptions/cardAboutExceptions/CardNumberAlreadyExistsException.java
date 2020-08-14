package am.basic.springTest.model.exceptions.cardAboutExceptions;

public class CardNumberAlreadyExistsException extends Exception {
    public CardNumberAlreadyExistsException(String message) {
        super(message);
    }


    public static void checkNumberSameness(boolean expresion, String message) throws CardNumberAlreadyExistsException {
        if (expresion) {
            throw new CardNumberAlreadyExistsException(message);
        }
    }
}
