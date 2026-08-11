package com.admission.portal.domain.application.service;

import com.admission.portal.domain.application.entity.Application;
import com.admission.portal.domain.application.entity.ApplicationStatus;
import com.admission.portal.domain.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Transactional
    public String submitApplicationV1(Long applicationId){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 원서입니다."));

        long currentSubmittedCount = applicationRepository.countByMajorAndStatus(application.getMajor(), ApplicationStatus.SUBMITTED);

        String examineeNumber = String.format("%s-%04d", application.getMajor(), currentSubmittedCount + 1);

        application.submit(examineeNumber);

        return examineeNumber;
    }


}
