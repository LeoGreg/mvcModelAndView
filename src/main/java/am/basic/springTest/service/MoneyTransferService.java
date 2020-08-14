package am.basic.springTest.service;

import am.basic.springTest.model.Card;
import am.basic.springTest.model.exceptions.cardAboutExceptions.BalanceIsNullException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.SumMinusValueException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface MoneyTransferService  {

    void doAllStuff(String senderNumber, String receiverNumber, double cash) throws BalanceIsNullException, SumMinusValueException, NotValidCardNumberException, CardNumberCheckingSecurityException;

    void transfer(String senderNumber, String receiverNumber, double cash);

    void analyzeOfBalance(String senderNumber) throws BalanceIsNullException;

    void analyzeSumDifference(String senderNumber, double cash) throws SumMinusValueException;

    String getInfo(String senderNumber, String receiverNumber);
}
