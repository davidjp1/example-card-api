package com.powell.cardapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationResponse {
    private boolean isValid;
    private List<String> validationErrors;
}
