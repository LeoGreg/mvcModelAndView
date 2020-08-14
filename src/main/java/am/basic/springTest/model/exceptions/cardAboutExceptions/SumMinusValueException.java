package am.basic.springTest.model.exceptions.cardAboutExceptions;

public class SumMinusValueException extends Exception {
    public SumMinusValueException(String message) {
        super(message);
    }


    public static void checkSumAfterTransfer(boolean expresion, String message) throws SumMinusValueException {
        if (expresion) {
            throw new SumMinusValueException(message);
        }
    }


}
