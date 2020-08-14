package am.basic.springTest.service.impl;

import am.basic.springTest.model.User;
import am.basic.springTest.model.exceptions.TestNotFoundException;
import am.basic.springTest.model.exceptions.cardAboutExceptions.ExpirationOutOfDateException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.CardNumberCheckingSecurityException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NoSuchACardNumberException;
import am.basic.springTest.model.exceptions.cardNumberValidationException.NotValidCardNumberException;
import am.basic.springTest.repository.CardRepository;
import am.basic.springTest.service.CardService;
import am.basic.springTest.model.Card;
import am.basic.springTest.model.exceptions.cardAboutExceptions.CardNumberAlreadyExistsException;
import am.basic.springTest.util.cardValidator.CardNumberValidation;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static am.basic.springTest.util.constants.Messages.CARD_SAMENESS_ERROR_MESSAGE;
import static am.basic.springTest.util.constants.Messages.NO_CARD_MESSAGE;
import static jdk.nashorn.internal.objects.NativeArray.concat;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private EntityManager em;

    @Autowired
    private CardRepository cardRepository;

    @Override
    @Transactional
    public void sign_up_card(Card card) throws CardNumberAlreadyExistsException, NotValidCardNumberException, ExpirationOutOfDateException {
        Card duplicate = cardRepository.getCardByCard_number(card.getCard_number());
        CardNumberAlreadyExistsException.checkNumberSameness(duplicate != null, CARD_SAMENESS_ERROR_MESSAGE);
        CardNumberValidation.validate(card.getCard_number());
        CardNumberValidation.isThroughDate(card.getExpiration_date());
        cardRepository.save(card);
    }

    @Override
    public Card sign_in(String card_number) throws NotValidCardNumberException, NoSuchACardNumberException, ExpirationOutOfDateException {
        CardNumberValidation.validate(card_number);
        Card card = cardRepository.getCardByCard_number(card_number);
        NoSuchACardNumberException.check(card == null, NO_CARD_MESSAGE);
        CardNumberValidation.isThroughDate(card.getExpiration_date());
        return cardRepository.getCardByCard_number(card_number);
    }

    @Override////
    public List<Card> searchByCardNumber(String number) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Card> cq = cb.createQuery(Card.class);

        Root<Card> card = cq.from(Card.class);
        Predicate carNumberPredict = cb.like(card.get("card_number"), "%" + number + "%");
        cq.where(carNumberPredict);

        TypedQuery<Card> query = em.createQuery(cq);
        return query.getResultList();
    }


    @Override
    @Transactional///////////
    public void deleteCardById(int id) {
        cardRepository.deleteById(id);
    }

/*    @Override
    public List<Card> search(Card cardLike) {
        List<Card> cardList = cardRepository.findAll(Example.of(cardLike));
        if (cardList.size() == 0) {
            throw new TestNotFoundException();
        }
        return cardList;
    }*/

    @Override///////////
    public List<Card> getAllCardsByUser_id(int user_id) {
        List<Card> cardList = cardRepository.getCardsByUser_id(user_id);
        return cardList;
    }

    @Override/////////////////
    public Optional<Card> getById(int id) {
        return cardRepository.findById(id);
    }

    @Override////////////////
    public Card getCardById(int id) {
        return cardRepository.findById(id).orElseThrow(() -> new TestNotFoundException());
    }

    @Override
    @Transactional//////////
    public void add(Card card) {
        cardRepository.save(card);
    }


    @Override/////
    public List<Card> getAll() {
        List<Card> cards = cardRepository.findAll();
        return cards;
    }

    @Override////////////////
    public Card getByCardNumber(String name) {
        Card card = cardRepository.getCardByCard_number(name);
        Optional<Card> optionalCard = Optional.ofNullable(card);
        if (!optionalCard.isPresent()) {
            throw new TestNotFoundException();
        }
        return card;
    }

    @Override
    @Transactional////
    public void updateCardInfo(Card card) {
        cardRepository.save(card);
    }


}
