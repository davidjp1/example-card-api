package com.powell.cardapi.service;

import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.User;
import com.powell.cardapi.repository.CardRepository;
import com.powell.cardapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.powell.cardapi.util.CardNumberGeneration.generateCVV;
import static com.powell.cardapi.util.CardNumberGeneration.generateCardNumber;
import static com.powell.cardapi.util.DateUtil.dateNowUTC;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.util.Objects.requireNonNull;
import static javax.imageio.ImageIO.read;

@Service
@Slf4j
public class CardService {

    private static final String CARD_NOT_FOUND = "Card Not Found";
    private static final String USER_NOT_FOUND = "User Not Found";
    private final BufferedImage baseImage;
    public static final String CARD_IS_EXPIRED = "Card is Expired";
    public static final String CARD_IS_FROZEN = "Card is Frozen";

    private CardRepository cardRepository;
    private UserRepository userRepository;
    private CardImageCreator cardImageCreator;

    public CardService(CardRepository cardRepository, UserRepository userRepository, CardImageCreator cardImageCreator) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardImageCreator = cardImageCreator;

        try {
            baseImage = read(requireNonNull(
                    CardService.class.getClassLoader()
                            .getResource("static/CreditCard.jpg")));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to Load Base Card Image");
        }
    }

    @Value("${card.timeToExpireInYears:4}")
    private int timeToExpireInYears;

    public Card generateCardForUser(String userId) {
        Card card = new Card();
        card.setCvv(generateCVV());
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(dateNowUTC().plus(timeToExpireInYears, YEARS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(USER_NOT_FOUND));

        card.setUser(user);
        return cardRepository.save(card);
    }

    public List<String> validateCard(String id) {
        List<String> validationErrors = new ArrayList<>();
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CARD_NOT_FOUND));

        if (dateNowUTC().isAfter(card.getExpiryDate())) {
            validationErrors.add(CARD_IS_EXPIRED);
        }
        if (card.isFrozen()) {
            validationErrors.add(CARD_IS_FROZEN);
        }
        return validationErrors;
    }

    public byte[] renderCardImage(String id) {
        log.debug("generating card image for id={}", id);
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CARD_NOT_FOUND));

        return cardImageCreator.renderCardImage(baseImage, card);
    }

    public Card getCard(String id) {
        return cardRepository.findById(id).orElse(null);
    }

    public void updateCardFrozen(String id, boolean frozen) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CARD_NOT_FOUND));
        card.setFrozen(frozen);
        cardRepository.save(card);
    }
}
