package am.basic.springTest.model.exceptions.cardAboutExceptions;

public class BalanceIsNullException extends Exception {
    public BalanceIsNullException(String message) {
        super(message);
    }


    public static void checkBalance(boolean expresion, String message) throws BalanceIsNullException {
        if (expresion) {
            throw new BalanceIsNullException(message);
        }
    }
}
