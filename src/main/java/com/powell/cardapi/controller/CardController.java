package com.powell.cardapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@Slf4j
public class CardController {

    @GetMapping("/v1")
    public void getCard() {

    }

    @PutMapping("/v1")
    public void updateCard() {

    }

    @PostMapping("/v1")
    public void addCard() {

    }

    @DeleteMapping("/v1")
    public void removeCard() {

    }

    @GetMapping("/v1/validate")
    public void isCardValid() {

    }
}
