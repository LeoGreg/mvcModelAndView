package am.basic.springTest.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String bank_branding;


    @Column(nullable = false, unique = true)
    @Nullable
    private String card_number;

    private Date expiration_date;

    @NotBlank
    private String payment_network_logo;
    @NotBlank
    private String bank_contact_information;

    private double balance;

    @Transient
    private String card_holder_name;

    private int user_id;//


}

