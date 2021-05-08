package com.powell.cardapi.service;

import com.powell.cardapi.configuration.CacheConfiguration;
import com.powell.cardapi.domain.Card;
import com.powell.cardapi.domain.User;
import com.powell.cardapi.repository.CardRepository;
import com.powell.cardapi.repository.UserRepository;
import com.powell.cardapi.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.CacheManager;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;

import static com.google.common.io.Resources.asByteSource;
import static com.google.common.io.Resources.getResource;
import static com.powell.cardapi.service.CardService.CARD_IS_EXPIRED;
import static com.powell.cardapi.service.CardService.CARD_IS_FROZEN;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalDate.class, ImageIO.class})
public class CardServiceTest {

    private static final String USER_ID = "aUserId123";
    private static final String CARD_ID = "aCardId123";
    private CardService sut;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private static final int EXPIRY_TIME_IN_YEARS = 5;
    private static final LocalDate TODAY = LocalDate.of(2021, 1, 1);;

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
                .withUserConfiguration(CardService.class, CardImageCreator.class, CacheConfiguration.class)
                .run(ctx -> sut = ctx.getBean(CardService.class));

        DateUtil.setClock(Clock.fixed(TODAY.atStartOfDay(UTC).toInstant(), UTC));
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
        when(userRepository.findById(USER_ID)).thenReturn(of(user));

        sut.generateCardForUser(USER_ID);

        ArgumentCaptor<Card> captor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    public void validateCard_validCard() {
        Card card = new Card();
        card.setId(CARD_ID);
        card.setFrozen(false);
        card.setExpiryDate(TODAY.plus(2, DAYS));
        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        assertThat(sut.validateCard(CARD_ID)).isEqualTo(emptyList());
    }

    @Test
    public void validateCard_invalidCard() {
        Card card = new Card();
        card.setId(CARD_ID);
        card.setFrozen(true);
        card.setExpiryDate(TODAY.minus(1, DAYS));
        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        assertThat(sut.validateCard(CARD_ID)).isEqualTo(asList(CARD_IS_EXPIRED, CARD_IS_FROZEN));
    }

    @Test
    public void renderCardImage_throwsIfNotFound() {
        assertThatThrownBy(() -> sut.renderCardImage(CARD_ID))
                .isInstanceOf(IllegalStateException.class);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Test
    public void renderCardImage_canRenderCard() throws IOException {

        Card card = new Card();
        card.setExpiryDate(LocalDate.of(2025, 5, 5));
        card.setCardNumber("517372333633558");
        User user = new User();
        user.setFirstName("David");
        user.setSecondName("Powell");
        card.setUser(user);

        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        byte[] expected = asByteSource(getResource("cards/expected-card.png")).read();
        assertThat(sut.renderCardImage(CARD_ID)).isEqualTo(expected);
    }

    @Test
    public void renderCardImage_throwsIfFailsToWriteImage() throws IOException {
        PowerMockito.mockStatic(ImageIO.class);
        PowerMockito.when(ImageIO.write(any(), any(), any(ByteArrayOutputStream.class))).thenThrow(new IOException());

        Card card = new Card();
        card.setExpiryDate(LocalDate.of(2025, 5, 5));
        card.setCardNumber("517372333633558");
        User user = new User();
        user.setFirstName("David");
        user.setSecondName("Powell");
        card.setUser(user);
        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        assertThatThrownBy(() -> sut.renderCardImage(CARD_ID))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void renderCardImage_cachesResults() throws IOException {
        PowerMockito.mockStatic(ImageIO.class);
        Card card = new Card();
        card.setExpiryDate(LocalDate.of(2025, 5, 5));
        card.setCardNumber("517372333633558");
        User user = new User();
        user.setFirstName("David");
        user.setSecondName("Powell");
        card.setUser(user);

        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        sut.renderCardImage(CARD_ID);
        sut.renderCardImage(CARD_ID);
        sut.renderCardImage(CARD_ID);

        PowerMockito.verifyStatic(ImageIO.class, times(1));
        ImageIO.write(any(), any(), any(ByteArrayOutputStream.class));
    }

    @Test
    public void getCard_getsCardFromDb() {
        Card card = new Card();
        card.setCardNumber("123");
        card.setId(CARD_ID);

        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        assertThat(sut.getCard(CARD_ID)).isEqualTo(card);
    }

    @Test
    public void getCard_returnsNullIfNoCard() {
        assertThat(sut.getCard(CARD_ID)).isEqualTo(null);
    }

    @Test
    public void updateCardFrozen_updatesCard() {
        Card card = new Card();
        card.setCardNumber("123");
        card.setId(CARD_ID);
        card.setFrozen(false);
        when(cardRepository.findById(CARD_ID)).thenReturn(of(card));

        sut.updateCardFrozen(CARD_ID, true);

        Card expected = new Card();
        expected.setCardNumber("123");
        expected.setId(CARD_ID);
        expected.setFrozen(true);
        verify(cardRepository).save(expected);
    }
    @Test
    public void updateCardFrozen_throwsIfCardNotFound() {
        assertThatThrownBy(() -> sut.updateCardFrozen(USER_ID, true))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void throwsOnStartup_ifImageFailsToLoad() throws IOException {
        PowerMockito.mockStatic(ImageIO.class);
        PowerMockito.when(ImageIO.read(any(URL.class))).thenThrow(new IOException());

        assertThatThrownBy(() -> new CardService(mock(CardRepository.class), mock(UserRepository.class), mock(CardImageCreator.class)))
                .isInstanceOf(IllegalStateException.class);
    }

}