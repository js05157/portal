package com.admission.portal.domain.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 전공별 수험번호 채번 테이블.
 * 전공당 행이 정확히 1개이며, 이 행을 PK로 비관적 락(SELECT ... FOR UPDATE)하여
 * 수험번호 발급을 직렬화한다.
 */
@Entity
@Table(name = "examinee_number_sequence")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamineeNumberSequence {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "major", length = 30)
    private Major major;

    @Column(name = "issued_count", nullable = false)
    private long issuedCount;

    public ExamineeNumberSequence(Major major) {
        this.major = major;
        this.issuedCount = 0L;
    }

    public long issueNext() {
        return ++this.issuedCount;
    }
}
