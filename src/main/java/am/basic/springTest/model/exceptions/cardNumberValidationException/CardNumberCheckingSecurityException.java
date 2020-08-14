package am.basic.springTest.model.exceptions.cardNumberValidationException;

public class CardNumberCheckingSecurityException extends Exception {
    public CardNumberCheckingSecurityException(String message) {
        super(message);
    }

    public static void check(boolean expresion, String message) throws CardNumberCheckingSecurityException {
        if (expresion) {
            throw new CardNumberCheckingSecurityException(message);
        }
    }
}
