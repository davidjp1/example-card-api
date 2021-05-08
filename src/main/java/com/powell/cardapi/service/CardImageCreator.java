package com.powell.cardapi.service;

import com.powell.cardapi.domain.Card;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.powell.cardapi.configuration.CacheConfiguration.IMAGE_CACHE;
import static java.time.format.DateTimeFormatter.ofPattern;

// This class is required to be separated out from CardService so that the proxy bean for the @Cacheable annotation is generated correctly
@Service
public class CardImageCreator {

    // Cache the result of this calculation as quite an intensive operation
    // If we don't want to store every users card image to the db, this is a nice middle-ground
    @Cacheable(value = IMAGE_CACHE, key = "#card")
    public byte[] renderCardImage(BufferedImage baseImage, Card card){
        BufferedImage bufferedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), baseImage.getType());
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(baseImage, 0, 0, null);
        g2d.setColor(Color.BLACK);
        // x:335, y:217
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
}
