package com.admission.portal.domain.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    A(100),
    B(90),
    C(80),
    D(70),
    E(60);

    private final int score;
}
