package com.powell.cardapi.service;

import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.User;
import com.powell.cardapi.domain.ValidationResponse;
import com.powell.cardapi.repository.CardRepository;
import com.powell.cardapi.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.powell.cardapi.util.CardNumberGeneration.generateCVV;
import static com.powell.cardapi.util.CardNumberGeneration.generateCardNumber;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.YEARS;

@Service
@RequiredArgsConstructor
public class CardService {

    @NonNull
    private CardRepository cardRepository;
    @NonNull
    private UserRepository userRepository;

    public Card generateCardForUser(String userId){
        Card card = new Card();
        card.setCvv(generateCVV());
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(LocalDate.now(UTC).plus(4, YEARS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User Not Found"));

        user.getCards().add(card);
        cardRepository.save(card);
        userRepository.save(user);
        return card;
    }

    public void addCard(Card c){
        cardRepository.save(c);
    }
    public void deleteCard(Card c){
        cardRepository.delete(c);
    }

    public List<String> validateCard(String id) {
        List<String> validationErrors = new ArrayList<>();
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Card Not Found"));

        if (LocalDate.now(UTC).isAfter(card.getExpiryDate())) {
            validationErrors.add("Card is Expired");
        }
        if (card.isFrozen()) {
            validationErrors.add("Card is Frozen");
        }
        return validationErrors;
    }

    public byte[] getCardImage(String id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Card Not Found"));

        BufferedImage bufferedImage = new BufferedImage(525 , 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0,0, 1050, 600);
        g2d.setColor(Color.BLACK);
        g2d.drawString(card.getCardNumber(), 100, 100);
        g2d.dispose();
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to write image to jpg", e);
        }
    }
}
