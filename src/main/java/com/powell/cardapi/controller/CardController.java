package com.powell.cardapi.controller;

import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.ValidationResponse;
import com.powell.cardapi.service.CardService;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/v1/{id}")
    @ApiOperation("Get Card details")
    public Card getCard(
            @PathVariable String id
    ) {
        log.info("getting card id={}", id);
        return cardService.getCard(id);
    }

    @PutMapping("/v1/{id}/freeze")
    @ApiOperation("Freeze or Unfreeze a Card")
    public void freezeCard(
            @PathVariable String id,
            @RequestParam boolean frozen
    ) {
        log.info("updating card freeze/unfreeze id={} frozen={}", id, frozen);
        cardService.updateCardFrozen(id, frozen);
    }

    @GetMapping("/v1/generate/{userId}")
    @ApiOperation("Request a new card for a given user ID")
    public Card requestCard(
            @PathVariable String userId
    ) {
        log.info("received request for new card for user={}", userId);
        return cardService.generateCardForUser(userId);
    }

    @GetMapping("/v1/{id}/validate")
    @ApiOperation("Check whether a card is valid for a given card ID, returns result and errors if they exist")
    public ValidationResponse isCardValid(
            @PathVariable String id
    ) {
        log.info("validating card id={}", id);
        List<String> validationErrors = cardService.validateCard(id);
        return new ValidationResponse(validationErrors.isEmpty(), validationErrors);
    }

    @GetMapping(value = "v1/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("Return a PNG image representation of the Card")
    public @ResponseBody byte[] getCardImage(
            @PathVariable String id
    ) {
        log.info("getting card image for id={}", id);
        return cardService.renderCardImage(id);
    }
}
