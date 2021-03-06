package com.powell.cardapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import static com.powell.cardapi.util.RandomUtils.randomUUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Immutable
public class Card extends ModificationTrackingEntity {

    @Id
    @Column(name = "ID")
    private String id = randomUUID();

    @Column(name = "CARD_NUMBER")
    private String cardNumber;
    @Column(name = "CVV")
    private String cvv;
    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;
    @Column(name = "FROZEN")
    private boolean isFrozen;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    // Optimistic lock to prevent dirty writes if two threads/instances try update card at the same time
    @Version
    @Column(name = "VERSION")
    @JsonIgnore
    private Long version = 0L;
}
