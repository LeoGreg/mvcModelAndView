package am.basic.springTest.service.impl;

import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;
import am.basic.springTest.repository.MoneyTransferRepository;
import am.basic.springTest.service.MoneyTransferService;
import am.basic.springTest.model.Card;
import am.basic.springTest.model.exceptions.cardAboutExceptions.BalanceIsNullException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.SumMinusValueException;
import am.basic.springTest.repository.MoneyTransferRepository;
import am.basic.springTest.service.MoneyTransferService;
import am.basic.springTest.util.cardValidator.CardNumberValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Autowired
    private MoneyTransferRepository moneyTransferRepository;

    @Override
    @Transactional
    public void doAllStuff(String senderNumber, String receiverNumber, double cash) throws BalanceIsNullException, SumMinusValueException, NotValidCardNumberException, CardNumberCheckingSecurityException {
        CardNumberValidation.checkNumber(senderNumber, receiverNumber);
        CardNumberValidation.validate(receiverNumber);
        CardNumberValidation.validate(senderNumber);
        analyzeOfBalance(senderNumber);
        analyzeSumDifference(senderNumber, cash);

        transfer(senderNumber, receiverNumber, cash);
        try {
            getInfo(senderNumber, receiverNumber);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transfer(String senderNumber, String receiverNumber, double cash) {
        Card sender = moneyTransferRepository.getCardByCard_number(senderNumber);
        Card receiver = moneyTransferRepository.getCardByCard_number(receiverNumber);
        sender.setBalance(sender.getBalance() - cash);
        receiver.setBalance(receiver.getBalance() + cash);
        moneyTransferRepository.save(sender);
        moneyTransferRepository.save(receiver);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyzeOfBalance(String senderNumber) throws BalanceIsNullException {
        Card sender = moneyTransferRepository.getCardByCard_number(senderNumber);
        if (sender.getBalance() == 0.0) {
            throw new BalanceIsNullException("balance is 0 please charge bill to fulfill transfer");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyzeSumDifference(String senderNumber, double cash) throws SumMinusValueException {
        Card sender = moneyTransferRepository.getCardByCard_number(senderNumber);
        if (sender.getBalance() - cash < 0) {
            throw new SumMinusValueException("transfer is called off,you do not have enough money on your card");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getInfo(String senderNumber, String receiverNumber) {
        Card sender = moneyTransferRepository.getCardByCard_number(senderNumber);
        Card receiver = moneyTransferRepository.getCardByCard_number(receiverNumber);
        String info = "Doing: money transfer\n" + "from: " + sender.getCard_holder_name() + "\n" + "to: " + receiver.getCard_holder_name() + "\n" + "date: " + new Date();
        return info;
    }

}

