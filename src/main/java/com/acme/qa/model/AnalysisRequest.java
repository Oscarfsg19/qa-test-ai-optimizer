package com.acme.qa.model;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record AnalysisRequest(
        @NotBlank String userStory,
        @NotBlank String acceptanceCriteria,
        List<TestCase> testCases
) {}
