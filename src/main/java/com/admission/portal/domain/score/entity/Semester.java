package com.admission.portal.domain.score.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Semester {
    SEMESTER_2_1(10),
    SEMESTER_2_2(10),
    SEMESTER_3_1(15),
    SEMESTER_3_2(15);

    private final int weight;
}
