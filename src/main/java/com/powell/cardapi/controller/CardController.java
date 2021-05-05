package com.powell.cardapi.controller;

import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.ValidationResponse;
import com.powell.cardapi.service.CardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@Slf4j
public class CardController {

    @NonNull
    private CardService cardService;

    @PostMapping("/v1")
    public Card requestCard(
            @RequestBody String userId
            ) {
        return cardService.generateCardForUser(userId);
    }

    @GetMapping("/v1/{id}")
    public Card getCard(
            @PathVariable String id
    ) {
        return cardService.getCard(id);
    }

    @GetMapping("/v1/{id}/validate")
    public ValidationResponse isCardValid(
        @PathVariable String id
    ) {
        List<String> validationErrors = cardService.validateCard(id);
        return new ValidationResponse(validationErrors.isEmpty(), validationErrors);
    }

    @GetMapping(value ="v1/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getCardImage(
            @PathVariable String id
    ) {
        try {
            return cardService.getCardImage(id);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to process card to Image", e);
        }
    }
}
