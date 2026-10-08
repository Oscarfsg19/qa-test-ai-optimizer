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
        String executiveSummary,

        // Nuevas métricas de análisis semántico
        int relevantCases,
        int partiallyRelevantCases,
        int irrelevantCases,
        int criteriaTotal,
        int criteriaCovered,
        int criteriaPartiallyCovered,
        int criteriaNotCovered,
        int coveragePercentage,

        // Nueva información detallada
        List<CriterionCoverage> criteriaCoverage,
        List<CaseAssessment> caseAssessments,
        List<MissingScenario> detailedMissingScenarios
) {

    /**
     * Constructor compatible con el código anterior.
     *
     * Esto permite que LocalQaAnalyzer y otras partes del proyecto
     * sigan utilizando el constructor original mientras incorporamos
     * las nuevas métricas de OpenAI.
     */
    public AnalysisResponse(
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
        this(
                mode,
                totalCases,
                duplicateGroups,
                traceableCases,
                highRiskCases,
                findings,
                missingScenarios,
                recommendations,
                executiveSummary,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                List.of(),
                List.of(),
                List.of()
        );
    }

    public record Finding(
            String type,
            String severity,
            String cases,
            String explanation,
            String action
    ) {}

    /**
     * Cobertura de un criterio de aceptación.
     */
    public record CriterionCoverage(
            String criterionId,
            String criterion,
            String status,
            List<String> relatedCases,
            String gap
    ) {}

    /**
     * Evaluación semántica de un caso respecto a la historia.
     */
    public record CaseAssessment(
            String caseId,
            String relevance,
            String risk,
            List<String> coveredCriteria,
            String explanation
    ) {}

    /**
     * Escenario que debería agregarse a la suite.
     */
    public record MissingScenario(
            String title,
            List<String> criteria,
            String objective,
            String steps,
            String expected,
            String priority,
            String type
    ) {}
}