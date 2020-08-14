
package am.basic.springTest.controller.card;

import am.basic.springTest.model.Card;
import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.cardAboutExceptions.BalanceIsNullException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.SumMinusValueException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;
import am.basic.springTest.service.CardService;
import am.basic.springTest.service.MoneyTransferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import static am.basic.springTest.util.constants.Messages.INTERNAL_ERROR_MESSAGE;
import static am.basic.springTest.util.constants.Messages.MONEY_SUCCESSFULLY_TRANSFERRED_MESSAGE;
import static am.basic.springTest.util.constants.Pages.*;
import static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY;

@Log4j2
@Controller
@RequestMapping("/secure")
public class MoneyTransferController {
    @Autowired
    private MoneyTransferService moneyTransferService;


    @PostMapping("/money-transfer/transfer")
    public ModelAndView transfer(@RequestParam String card_numberFrom,
                                 @RequestParam String cash,
                                 @RequestParam String card_numberTo) {
        try {
            double cashAll = Double.parseDouble(cash);
            moneyTransferService.doAllStuff(card_numberFrom, card_numberTo, cashAll);
            log.info("<<<transfer is done:");
            return new ModelAndView(MONEY_TRANSFER_PAGE, MESSAGE_ATTRIBUTE_KEY, MONEY_SUCCESSFULLY_TRANSFERRED_MESSAGE);
        } catch (BalanceIsNullException e) {
            log.warn(">>>trying to transfer money in the case when balance is 0:");
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (NotValidCardNumberException e) {
            log.warn(">>>during transfer got some not valid card number:");
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (CardNumberCheckingSecurityException e) {
            log.warn(">>>transfer's been called off cuz user's given numbers --> numberFrom numberTo were equals:");
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (SumMinusValueException e) {
            log.warn(">>>transfer can't be done cuz of not enough balance:");
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(">>>some undone error on transfer page:");
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        }
    }
}

