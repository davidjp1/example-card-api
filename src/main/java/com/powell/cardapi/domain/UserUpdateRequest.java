package com.powell.cardapi.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.Currency;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String secondName;
    private LocalDate birthDate;
    private Currency preferredCurrency;
}
