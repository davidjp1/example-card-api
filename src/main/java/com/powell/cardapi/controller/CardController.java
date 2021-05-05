package com.powell.cardapi.controller;

import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.ValidationResponse;
import com.powell.cardapi.service.CardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        log.info("received request for new card for user={}", userId);
        return cardService.generateCardForUser(userId);
    }

    @GetMapping("/v1/{id}")
    public Card getCard(
            @PathVariable String id
    ) {
        log.info("getting card id={}", id);
        return cardService.getCard(id);
    }

    @PutMapping("/v1/{id}/frozen")
    public Card freezeCard(
            @PathVariable String id,
            @RequestParam boolean frozen
    ) {
        log.info("updating card freeze/unfreeze id={} frozen={}", id, frozen);
        return cardService.updateCardFrozen(id, frozen);
    }

    @GetMapping("/v1/{id}/validate")
    public ValidationResponse isCardValid(
            @PathVariable String id
    ) {
        log.info("validating card id={}", id);
        List<String> validationErrors = cardService.validateCard(id);
        return new ValidationResponse(validationErrors.isEmpty(), validationErrors);
    }

    @GetMapping(value ="v1/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getCardImage(
            @PathVariable String id
    ) {
        log.info("getting card image for id={}", id);
        return cardService.renderCardImage(id);
    }
}
