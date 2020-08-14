package am.basic.springTest.repository;

import am.basic.springTest.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyTransferRepository extends JpaRepository<Card, Integer> {

    @Query(nativeQuery = true,value = "SELECT *  FROM card WHERE card_number=:CardNumber ")
    Card getCardByCard_number(@Param("CardNumber") String card_number);
}
