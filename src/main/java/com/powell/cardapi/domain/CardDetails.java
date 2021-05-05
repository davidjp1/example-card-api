package com.powell.cardapi.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CardDetails {

    @Id
    @Column(name = "ID")
    String id;

//    @NonNull
//    private User cardOwner;

    // Optimistic lock to prevent dirty writes if two threads/instances try update card at the same time
    @Version
    @Column(name = "VERSION")
    @EqualsAndHashCode.Exclude
    private Long version = 0L;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;
    @Column(name = "CVV")
    private String cvv;
    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;
}
