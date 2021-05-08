package com.powell.cardapi.service;


import com.powell.cardapi.domain.User;
import com.powell.cardapi.domain.UserUpdateRequest;
import com.powell.cardapi.repository.UserRepository;
import com.powell.cardapi.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RandomUtils.class)
public class UserServiceTest {

    private UserService sut;
    private UserRepository repository;

    private UserUpdateRequest updateRequest;
    private static final String USER_ID = "aUserId";

    @Before
    public void setup(){
        repository = mock(UserRepository.class);
        sut = new UserService(repository);

        updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("Tim");
        updateRequest.setSecondName("Henson");
        updateRequest.setBirthDate(LocalDate.of(1995, 5, 10));
        updateRequest.setPreferredCurrency(Currency.getInstance("USD"));

        PowerMockito.mock(RandomUtils.class);
        PowerMockito.stub(method(RandomUtils.class, "randomUUID")).toReturn(USER_ID);
    }

    @Test
    public void getUsers_returnsListOfUserIds() {
        List<User> users = asList(mockUser("Rick", "Astley"), mockUser("David", "Powell"), mockUser("Jeff", "Bezos"));
        when(repository.findAll()).thenReturn(users);
        assertThat(sut.getUsers()).isEqualTo(users.stream().map(User::getId).collect(toList()));
    }

    @Test
    public void getUser_getsUserFromDb() {
        String id = "aUserId";
        User user = mockUser("David", "Powell");
        when(repository.findById(id)).thenReturn(of(user));

        assertThat(sut.getUser(id)).isEqualTo(user);
    }

    @Test
    public void getUser_returnsNullIfNotFound() {
        String id = "aUserId";
        when(repository.findById(id)).thenReturn(empty());

        assertThat(sut.getUser(id)).isNull();
    }

    @Test
    public void addUser_addsUserWithCorrectDetails() {

        User expected = new User();
        expected.setFirstName(updateRequest.getFirstName());
        expected.setSecondName(updateRequest.getSecondName());
        expected.setPreferredCurrency(updateRequest.getPreferredCurrency());
        expected.setBirthDate(updateRequest.getBirthDate());
        expected.setCards(emptyList());
        expected.setId(USER_ID);

        sut.addUser(updateRequest);

        verify(repository).save(expected);
    }

    @Test
    public void updateUser_throwsIfNotFound() {
        assertThatThrownBy(() -> sut.updateUser("anId", updateRequest))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateUser_updatesUserCorrectly_allFieldsUpdated() {

        User oldUser = new User();
        oldUser.setFirstName("Foo");
        oldUser.setSecondName("Bar");
        oldUser.setPreferredCurrency(Currency.getInstance("GBP"));
        oldUser.setBirthDate(LocalDate.of(2000,1,1));
        oldUser.setCards(emptyList());
        oldUser.setId(USER_ID);
        when(repository.findById(USER_ID)).thenReturn(of(oldUser));

        sut.updateUser(USER_ID, updateRequest);

        User expected = new User();
        expected.setId(USER_ID);
        expected.setBirthDate(updateRequest.getBirthDate());
        expected.setPreferredCurrency(updateRequest.getPreferredCurrency());
        expected.setFirstName(updateRequest.getFirstName());
        expected.setSecondName(updateRequest.getSecondName());
        expected.setCards(emptyList());

        verify(repository).save(expected);
    }

    @Test
    public void updateUser_updatesUserCorrectly_noFieldsUpdated() {

        User expected = new User();
        expected.setFirstName("Foo");
        expected.setSecondName("Bar");
        expected.setPreferredCurrency(Currency.getInstance("GBP"));
        expected.setBirthDate(LocalDate.of(2000,1,1));
        expected.setCards(emptyList());
        expected.setId(USER_ID);
        when(repository.findById(USER_ID)).thenReturn(of(expected));

        sut.updateUser(USER_ID, new UserUpdateRequest());
        // Request with all null values, should result in no changes to user
        verify(repository).save(expected);
    }

    @Test
    public void deleteUser_deletesUsingId(){
        sut.deleteUser(USER_ID);
        verify(repository).deleteById(USER_ID);
    }

    private User mockUser(String firstName, String secondName){
        User user = new User();
        user.setFirstName(firstName);
        user.setFirstName(secondName);
        user.setBirthDate(LocalDate.of(2020, 2, 2));
        user.setPreferredCurrency(Currency.getInstance("GBP"));
        return user;
    }

}