package am.basic.springTest.controller.card;


import am.basic.springTest.model.Card;
import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.cardAboutExceptions.CardNumberAlreadyExistsException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.ExpirationOutOfDateException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NoSuchACardNumberException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;
import am.basic.springTest.service.CardService;
import jdk.nashorn.internal.ir.Optimistic;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static am.basic.springTest.util.constants.Messages.INTERNAL_ERROR_MESSAGE;
import static am.basic.springTest.util.constants.Pages.*;
import static am.basic.springTest.util.constants.ParameterKeys.*;

@Log4j2
@Controller
@RequestMapping("/secure")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/cards")
    public ModelAndView getCardsPage(@SessionAttribute("user") User user) {
        try {
            log.info("<<<almost getting card page:");
            List<Card> cards = cardService.getAllCardsByUser_id(user.getId());
            log.info("<<<just got all cards:");
            return new ModelAndView(CARD_PAGE, CARD_REQUEST_KEY, cards);
        } catch (RuntimeException e) {
            log.error(">>>some unexpected error on about sending to show cards:");
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);
        }

    }

/*
     @RequestParam( value = "CREATE_DATE", required=false) @DateTimeFormat("dd-MM-yyyy") Date date

     */

    @PostMapping("/cards/sign_up")
    public ModelAndView sign_up(@SessionAttribute("user") User user,
                                @RequestParam String bank_branding,
                                @RequestParam String card_number,
                                @RequestParam(value = "expiration_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date expiration_date,
                                @RequestParam String payment_network_logo,
                                @RequestParam String bank_contact_information,
                                @RequestParam String balance) {
        try {
            Card card = new Card();
            log.info("<<<just created card example:");
            card.setBalance(Double.parseDouble(balance));
            log.info("<<<balance is set:");
            card.setCard_holder_name(user.getName());
            log.info("<<<holder naem is set:");
            card.setBank_branding(bank_branding);
            log.info("<<<bank brand is set:");
            card.setBank_contact_information(bank_contact_information);
            log.info("<<<contact is set:");
            card.setCard_number(card_number);
            log.info("<<<card number is set:");
            card.setExpiration_date(expiration_date);
            log.info("<<<expiration date is set:");
            card.setPayment_network_logo(payment_network_logo);
            log.info("<<<logo is set:");
            card.setUser_id(user.getId());
            log.info("<<<user id is set:");
            cardService.sign_up_card(card);
            log.info("<<<card is signed up:");
            return getCardsPage(user);
        } catch (CardNumberAlreadyExistsException e) {
            log.warn(">>>can't create card with the same card_number:");
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (NotValidCardNumberException e) {
            log.warn(">>>card-sign-up page__>card number isn't 16:");
            log.warn(">>>card number not valid :{}", card_number);
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (RuntimeException e) {
            log.error(">>>during card-sign-up process some unexpected error:");
            e.printStackTrace();
            return new ModelAndView(HOME_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (ExpirationOutOfDateException e) {
            log.warn(">>>trying to sign up over expired card:");
            log.warn(">>>expiration date :{}", expiration_date);
            e.printStackTrace();
            return new ModelAndView(CARD_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());

        }
    }

    @PostMapping("/cards/edit")
    public ModelAndView edit(@SessionAttribute("user") User user,
                             @RequestParam Integer id,
                             @RequestParam String bank_branding,
                             @RequestParam String card_number,
                             @RequestParam(value = "expiration_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date expiration_date,
                             @RequestParam String payment_network_logo,
                             @RequestParam String bank_contact_information,
                             @RequestParam String balance,
                             @RequestParam String submit) {
        try {
            if (submit.equalsIgnoreCase("DELETE")) {
                cardService.deleteCardById(id);
                log.info("<<<card has been deleted:");
            } else {
                Card card = cardService.getCardById(id);
                card.setCard_holder_name(user.getName());
                card.setUser_id(user.getId());
                card.setBank_branding(bank_branding);
                card.setCard_number(card_number);
                card.setExpiration_date(expiration_date);
                card.setPayment_network_logo(payment_network_logo);
                card.setBank_contact_information(bank_contact_information);
                card.setBalance(Double.parseDouble(balance));
                cardService.updateCardInfo(card);
                log.info("<<<card info's been updated");
            }
            return getCardsPage(user);
        } catch (RuntimeException e) {
            log.error(">>>some exception by editing  card info:");
            e.printStackTrace();
            return new ModelAndView(CARD_PAGE, MESSAGE_ATTRIBUTE_KEY, INTERNAL_ERROR_MESSAGE);

        }
    }

    @PostMapping("/card-sign-in/sign-in")
    public ModelAndView sign_in(
            @RequestParam String card_number) {
        try {
            Card card = cardService.sign_in(card_number);
            log.info("<<<card is signed up");
            ModelAndView modelAndView = new ModelAndView(MONEY_TRANSFER_PAGE);
            modelAndView.addObject(NUMBER_REQUEST_KEY, card.getCard_number());
            return modelAndView;
        } catch (RuntimeException e) {
            log.error(">>>some undone error during card sign-in:");
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (ExpirationOutOfDateException e) {
            log.warn(">>>during card-sign-in card-number is not through :");
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (NotValidCardNumberException e) {
            log.warn(">>>error in sign-in because of card number length:");
            log.warn(">>>not valid card number:{}", card_number);
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        } catch (NoSuchACardNumberException e) {
            log.warn(">>> there isn't such a card with given number in sign-in level:");
            log.warn(">>>wrong number:{}", card_number);
            e.printStackTrace();
            return new ModelAndView(CARD_SIGN_IN_PAGE, MESSAGE_ATTRIBUTE_KEY, e.getMessage());
        }

    }
}




