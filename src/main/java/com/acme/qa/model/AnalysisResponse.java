package com.acme.qa.model;

import java.util.List;

public record AnalysisResponse(
        String mode,
        int totalCases,
        int duplicateGroups,
        int traceableCases,
        int highRiskCases,
        List<Finding> findings,
        List<String> missingScenarios,
        List<String> recommendations,
        String executiveSummary
) {
    public record Finding(
            String type,
            String severity,
            String cases,
            String explanation,
            String action
    ) {}
}
