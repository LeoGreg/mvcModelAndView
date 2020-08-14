package am.basic.springTest.model;

import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@Entity
@Table(name = "user")
public class User {


    public enum Gender {
        MALE, FEMALE,OTHER;
   }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;

    private String surname;

    private String code;

    @NotBlank
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank
    private String password;

    @Column(name = "status", nullable = false)
    private int status;

    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,5}")
    private String g_mail;
//
    @Transient
    private String dataOfBirth;


   private String gender;

    private String country;
}
