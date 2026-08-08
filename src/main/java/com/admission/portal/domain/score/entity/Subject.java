package com.admission.portal.domain.score.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Subject {
    KOREA(15),
    MATH(15),
    ENGLISH(15),
    SCIENCE(10),
    SOCIAL(10),
    HISTORY(10);

    private final int weight;
}
