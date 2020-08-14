
package am.basic.springTest.service;

import am.basic.springTest.model.Card;
import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.cardAboutExceptions.CardNumberAlreadyExistsException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.ExpirationOutOfDateException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NoSuchACardNumberException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CardService {

    void sign_up_card(Card card) throws CardNumberAlreadyExistsException, NotValidCardNumberException, ExpirationOutOfDateException;


    Card sign_in(String card_number) throws NotValidCardNumberException, NoSuchACardNumberException, ExpirationOutOfDateException;



    List<Card> searchByCardNumber(String number);

    void deleteCardById(int id);


//    List<Card> search(Card cardLike);

    /*List<Card> search(Card cardLike);*/

    List<Card> getAllCardsByUser_id(int user_id);


    Optional<Card> getById(int id);

    Card getCardById(int id);

    void add(Card card);

    List<Card> getAll();

    Card getByCardNumber(String num);


    @Transactional
    void updateCardInfo(Card card);
}
