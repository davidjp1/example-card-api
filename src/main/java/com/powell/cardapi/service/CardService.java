package com.powell.cardapi.service;

import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.User;
import com.powell.cardapi.repository.CardRepository;
import com.powell.cardapi.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.powell.cardapi.configuration.CacheConfiguration.IMAGE_CACHE;
import static com.powell.cardapi.util.CardNumberGeneration.generateCVV;
import static com.powell.cardapi.util.CardNumberGeneration.generateCardNumber;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.YEARS;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private static final String CARD_NOT_FOUND = "Card Not Found";
    private static final String USER_NOT_FOUND = "User Not Found";
    private static final BufferedImage BASE_IMAGE;

    static {
        try {
            BASE_IMAGE = ImageIO.read(CardService.class.getClassLoader().getResource("static/CreditCard.jpg"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to Load Base Card Image");
        }
    }

    @NonNull
    private CardRepository cardRepository;
    @NonNull
    private UserRepository userRepository;

    @Value("${card.timeToExpireInYears:4}")
    private int timeToExpireInYears;

    public Card generateCardForUser(String userId) {
        Card card = new Card();
        card.setCvv(generateCVV());
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(LocalDate.now(UTC).plus(timeToExpireInYears, YEARS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(USER_NOT_FOUND));

        card.setUser(user);
        return cardRepository.save(card);
    }

    public List<String> validateCard(String id) {
        List<String> validationErrors = new ArrayList<>();
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CARD_NOT_FOUND));

        if (LocalDate.now(UTC).isAfter(card.getExpiryDate())) {
            validationErrors.add("Card is Expired");
        }
        if (card.isFrozen()) {
            validationErrors.add("Card is Frozen");
        }
        return validationErrors;
    }

    // Cache the result of this calculation as quite an intensive operation
    // If we don't want to store every users card image to the db, this is a nice middle-ground
    @Cacheable(value = IMAGE_CACHE, key = "#id")
    public byte[] renderCardImage(String id) {
        log.debug("generating card image for id={}", id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CARD_NOT_FOUND));
        // x:335, y:217
        BufferedImage bufferedImage = new BufferedImage(BASE_IMAGE.getWidth(), BASE_IMAGE.getHeight(), BASE_IMAGE.getType());
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(BASE_IMAGE, 0, 0, null);
        g2d.setColor(Color.BLACK);
        g2d.drawString(card.getCardNumber().substring(0, 4), 90, 130);
        g2d.drawString(card.getCardNumber().substring(4, 10), 145, 130);
        g2d.drawString(card.getCardNumber().substring(10, 15), 210, 130);
        g2d.drawString(card.getExpiryDate().format(ofPattern("dd-MMM-YYYY")), 160, 170);
        g2d.drawString(String.format("%s %s", card.getUser().getFirstName(), card.getUser().getSecondName()), 50, 190);
        g2d.dispose();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write image", e);
        }
    }

    public Card getCard(String id) {
        return cardRepository.findById(id).orElse(null);
    }

    public Card updateCardFrozen(String id, boolean frozen) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CARD_NOT_FOUND));
        card.setFrozen(frozen);
        return card;
    }
}
