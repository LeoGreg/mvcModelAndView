
package am.basic.springTest.repository;

import am.basic.springTest.model.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM card WHERE user_id=:userId ")
    List<Card> getCardsByUser_id(@Param("userId") int user_id);


    @Query(nativeQuery = true, value = "SELECT *  FROM card WHERE card_number=:CardNumber ")
    Card getCardByCard_number(@Param("CardNumber") String card_number);


    Card getCardById(int id);







}

