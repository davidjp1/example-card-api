package com.powell.cardapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "SECOND_NAME")
    private String secondName;
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;
    @Column(name = "PREFERRED_CURRENCY")
    private Currency preferredCurrency;

    @OneToMany(mappedBy = "user")
    private List<Card> cards = new ArrayList<>();

    // Optimistic lock to prevent dirty writes if two threads/instances try update card at the same time
    @Version
    @Column(name = "VERSION")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Long version = 0L;
}
