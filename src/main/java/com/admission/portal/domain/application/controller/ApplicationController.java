package com.admission.portal.domain.application.controller;

import com.admission.portal.domain.application.dto.request.ApplicationSaveRequest;
import com.admission.portal.domain.application.dto.response.ApplicationDetailResponse;
import com.admission.portal.domain.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/me")
    public ResponseEntity<ApplicationDetailResponse> getMyApplication(@AuthenticationPrincipal Long userId) {
        return applicationService.getMyApplication(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<Void> saveDraft(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ApplicationSaveRequest request
    ) {
        applicationService.saveDraft(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submit(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ApplicationSaveRequest request
    ) {
        applicationService.submit(userId, request);
        return ResponseEntity.noContent().build();
    }
}
