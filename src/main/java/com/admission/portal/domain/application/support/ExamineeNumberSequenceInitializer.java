package com.admission.portal.domain.application.support;

import com.admission.portal.domain.application.entity.ExamineeNumberSequence;
import com.admission.portal.domain.application.entity.Major;
import com.admission.portal.domain.application.repository.ExamineeNumberSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 전공별 채번 행을 애플리케이션 기동 시 보장한다.
 * 채번 시점에 행을 만들면 최초 1건에서 삽입 경합이 생기므로, 시작 시 미리 만들어 둔다.
 */
@Component
@RequiredArgsConstructor
public class ExamineeNumberSequenceInitializer implements ApplicationRunner {

    private final ExamineeNumberSequenceRepository examineeNumberSequenceRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (Major major : Major.values()) {
            if (!examineeNumberSequenceRepository.existsById(major)) {
                examineeNumberSequenceRepository.save(new ExamineeNumberSequence(major));
            }
        }
    }
}
