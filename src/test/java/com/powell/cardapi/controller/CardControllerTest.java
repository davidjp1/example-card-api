package com.powell.cardapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.ValidationResponse;
import com.powell.cardapi.service.CardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CardController.class)
public class CardControllerTest {

    @MockBean
    private CardService cardService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/card";
    private static final String CARD_ID = "aCardId123";

    @Test
    public void requestCard_callsUnderlyingApi() throws Exception {
        Card card = new Card();
        card.setId(CARD_ID);
        card.setCardNumber("1234");

        when(cardService.generateCardForUser(CARD_ID)).thenReturn(card);

        mockMvc.perform(get(format("%s/%s/%s/%s", BASE_URL, "v1", "generate", CARD_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(card)));
    }

    @Test
    public void getCard_callsUnderlyingApi() throws Exception {
        Card card = new Card();
        card.setId(CARD_ID);
        card.setCardNumber("1234");

        when(cardService.getCard(CARD_ID)).thenReturn(card);

        mockMvc.perform(get(format("%s/%s/%s", BASE_URL, "v1", CARD_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(card)));
    }

    @Test
    public void freezeCard_callsUnderlyingApi() throws Exception {

        mockMvc.perform(put(format("%s/%s/%s/%s?%s=%s", BASE_URL, "v1", CARD_ID, "freeze", "frozen", "true")))
                .andExpect(status().isOk());

        verify(cardService).updateCardFrozen(CARD_ID, true);
    }

    @Test
    public void isCardValid_noValidationErrors() throws Exception {
        when(cardService.validateCard(CARD_ID)).thenReturn(emptyList());

        mockMvc.perform(get(format("%s/%s/%s/%s", BASE_URL, "v1", CARD_ID, "validate")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ValidationResponse(true, emptyList()))));
    }

    @Test
    public void isCardValid_withValidationErrors() throws Exception {
        List<String> errors = asList("an error", "something else wrong");
        when(cardService.validateCard(CARD_ID)).thenReturn(errors);

        mockMvc.perform(get(format("%s/%s/%s/%s", BASE_URL, "v1", CARD_ID, "validate")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ValidationResponse(false, errors))));
    }

    @Test
    public void getCardImage_callsUnderlyingApi() throws Exception {
        byte[] img = "fakeImage".getBytes();
        when(cardService.renderCardImage(CARD_ID)).thenReturn(img);

        mockMvc.perform(get(format("%s/%s/%s/%s", BASE_URL, "v1", CARD_ID, "image")))
                .andExpect(status().isOk())
                .andExpect(content().bytes(img))
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
    }
}