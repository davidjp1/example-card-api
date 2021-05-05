package com.powell.cardapi.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.Currency;

@Data
public class User {
    @NonNull
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String secondName;
    @NonNull
    private Currency preferredCurrency;
}
