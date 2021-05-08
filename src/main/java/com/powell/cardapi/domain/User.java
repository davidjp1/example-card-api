package com.powell.cardapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static com.powell.cardapi.util.RandomUtils.randomUUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends ModificationTrackingEntity {

    @Id
    @Column(name = "ID")
    private String id = randomUUID();
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
    @JsonIgnore
    private Long version = 0L;
}
