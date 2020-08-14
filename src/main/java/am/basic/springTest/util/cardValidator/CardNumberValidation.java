package am.basic.springTest.util.cardValidator;

import am.basic.springTest.model.exceptions.cardAboutExceptions.CardNumberAlreadyExistsException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.ExpirationOutOfDateException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;

import java.util.Date;

import static am.basic.springTest.util.constants.Messages.*;

public class CardNumberValidation {
    private static int defaultNumberSize = 16;

    public static void validate(String number) throws NotValidCardNumberException {
        NotValidCardNumberException.isNotValid(number.length() != defaultNumberSize, NOT_VALID_CARD_NUMBER_MESSAGE);
    }

    public static void checkNumber(String numberFrom, String numberTo) throws CardNumberCheckingSecurityException {
        CardNumberCheckingSecurityException.check(numberFrom.equals(numberTo), THE_SAME_NUMBER_MESSAGE);
    }

    public static void isThroughDate(Date date) throws  ExpirationOutOfDateException {
        ExpirationOutOfDateException.isThroughDate((date.getTime() - new Date().getTime())<0, CARD_EXPIRATION_MESSAGE);
    }
}
