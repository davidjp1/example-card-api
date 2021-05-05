package com.powell.cardapi.service;

import com.powell.cardapi.domain.User;
import com.powell.cardapi.repository.CardRepository;
import com.powell.cardapi.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardServiceTest {

    private static final String USER_ID = "aUserId123";
    private CardService sut;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private static final int EXPIRY_TIME_IN_YEARS = 5;

    @Before
    public void setup() {
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);

        new ApplicationContextRunner()
                .withPropertyValues("card.timeToExpireInYears=" + EXPIRY_TIME_IN_YEARS)
                .withInitializer(init -> {
                    init.getBeanFactory().registerSingleton("cardRepository", cardRepository);
                    init.getBeanFactory().registerSingleton("userRepository", userRepository);
                })
                .withUserConfiguration(CardService.class)
                .run(ctx -> sut = ctx.getBean(CardService.class));
    }

    @Test
    public void generateCardForUser_throwsIfUserNotFound() {
        assertThatThrownBy(() -> sut.generateCardForUser(USER_ID))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void generateCardForUser_generatesCardAsExpectedForValidUser() {
        User user = new User();
        user.setFirstName("David");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        assertThat(sut.generateCardForUser(USER_ID)).isEqualTo(user);
    }

    @Test
    public void validateCard_validCard() {
        // TODO
    }

    @Test
    public void validateCard_cardOutOfDate() {
        // TODO
    }

    @Test
    public void validateCard_cardFrozen() {
        // TODO
    }

    @Test
    public void validateCard_cardFrozenAndOutOfDate() {
        // TODO
    }

    @Test
    public void renderCardImage_throwsIfNotFound() {
        // TODO
    }

    @Test
    public void renderCardImage_canRenderCard() {
        // TODO
    }

    @Test
    public void renderCardImage_throwsIfFailsToWriteImage() {
        // TODO
    }

    @Test
    public void renderCardImage_cachesResults() {
        // TODO
    }

    @Test
    public void getCard_getsCardFromDb() {
        // TODO
    }

    @Test
    public void getCard_returnsNullIfNoCard() {
        // TODO
    }

    @Test
    public void updateCardFrozen_updatesCardAndReturnsUpdatedValue() {
        // TODO
    }

    @Test
    public void throwsOnStartup_ifImageFailsToLoad() {
        // TODO
    }

}