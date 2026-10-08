package com.acme.qa.service;

import com.acme.qa.model.AnalysisRequest;
import com.acme.qa.model.AnalysisResponse;
import com.acme.qa.model.TestCase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Conditional(OpenAiQaAnalyzer.OpenAiEnabledCondition.class)
public class OpenAiQaAnalyzer {

    private final OpenAIClient client;
    private final ObjectMapper mapper;

    public OpenAiQaAnalyzer(
            OpenAIClient client,
            ObjectMapper mapper
    ) {
        this.client = client;
        this.mapper = mapper;
    }

    public AnalysisResponse analyze(AnalysisRequest request) {

        String prompt = buildPrompt(request);

        ResponseCreateParams params =
                ResponseCreateParams.builder()
                        .model("gpt-5.6-luna")
                        .input(prompt)
                        .build();

        Response response =
                client.responses().create(params);

        String output =
                extractOutputText(response);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("========== RESPUESTA CRUDA DE OPENAI ===========");
        System.out.println("=================================================");
        System.out.println(output);
        System.out.println("=================================================");
        System.out.println();

        return parseAnalysisResponse(
                output,
                request.testCases()
        );
    }

    private String buildPrompt(AnalysisRequest request) {

        List<TestCase> testCases =
                request.testCases() == null
                        ? List.of()
                        : request.testCases();

        String casesJson;

        try {

            casesJson =
                    mapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(testCases);

        } catch (Exception e) {

            casesJson = "[]";
        }

        return """
                Actúa como un QA Lead Senior especializado en:

                - análisis de cobertura funcional;
                - trazabilidad entre requisitos y casos de prueba;
                - diseño de pruebas;
                - análisis de riesgos;
                - detección de duplicados;
                - identificación de gaps de cobertura.

                Tu tarea es analizar una HISTORIA DE USUARIO,
                sus CRITERIOS DE ACEPTACIÓN y una SUITE DE CASOS
                DE PRUEBA EXISTENTE.

                ==================================================
                OBJETIVO PRINCIPAL
                ==================================================

                Determina si la suite existente realmente prueba
                la funcionalidad descrita en la historia de usuario.

                NO asumas que una suite tiene cobertura solamente
                porque contiene muchos casos.

                Un caso solamente cubre un criterio cuando sus pasos,
                objetivo y resultado esperado prueban directamente
                el comportamiento definido por dicho criterio.

                ==================================================
                1. COBERTURA POR CRITERIO
                ==================================================

                Analiza TODOS los criterios de aceptación.

                Para cada criterio genera:

                - criterionId
                - criterion
                - status
                - relatedCases
                - gap

                Los estados permitidos son:

                COVERED
                PARTIALLY_COVERED
                NOT_COVERED

                Usa identificadores:

                CA-01
                CA-02
                CA-03
                ...

                Si ningún caso prueba un criterio:

                status = NOT_COVERED

                y explica qué falta probar.

                ==================================================
                2. RELEVANCIA DE LOS CASOS
                ==================================================

                Evalúa TODOS los casos de prueba.

                Cada caso debe clasificarse como:

                RELEVANT
                PARTIALLY_RELEVANT
                IRRELEVANT

                RELEVANT:
                El caso prueba directamente uno o más criterios.

                PARTIALLY_RELEVANT:
                El caso tiene alguna relación funcional con la historia
                pero no cubre completamente el comportamiento requerido.

                IRRELEVANT:
                El caso pertenece a otra funcionalidad o no aporta
                cobertura útil para esta historia.

                Para cada caso devuelve:

                - caseId
                - relevance
                - risk
                - coveredCriteria
                - explanation

                ==================================================
                3. RIESGO
                ==================================================

                Evalúa el riesgo EN EL CONTEXTO DE LA HISTORIA.

                No confundas la prioridad original del caso con
                el riesgo funcional de la historia.

                Usa:

                HIGH
                MEDIUM
                LOW
                NONE

                Un caso IRRELEVANT DEBE tener riesgo NONE
                respecto a esta historia.

                ==================================================
                4. DUPLICADOS
                ==================================================

                Detecta grupos de casos que tengan:

                - mismos pasos;
                - mismo objetivo;
                - mismo resultado esperado;
                - o una similitud funcional suficientemente alta.

                No marques como duplicado únicamente porque dos casos
                tengan palabras parecidas.

                ==================================================
                5. ESCENARIOS FALTANTES
                ==================================================

                Esta es una parte MUY IMPORTANTE del análisis.

                Debes generar escenarios de prueba que actualmente
                NO estén cubiertos.

                Para cada escenario incluye:

                - title
                - criteria
                - objective
                - steps
                - expected
                - priority
                - type

                Los tipos permitidos son:

                POSITIVE
                NEGATIVE
                BOUNDARY
                SECURITY
                CONCURRENCY
                RESILIENCE
                AUDIT
                NOTIFICATION
                SYNCHRONIZATION

                Considera cuando sean aplicables:

                - escenarios positivos;
                - escenarios negativos;
                - límites;
                - permisos;
                - validaciones;
                - errores;
                - concurrencia;
                - sincronización;
                - pérdida de conexión;
                - auditoría;
                - notificaciones;
                - diferentes dispositivos o sesiones.

                Si la suite actual no cubre la historia,
                DEBES generar escenarios faltantes.

                Si TODOS los casos pertenecen a otra funcionalidad,
                debes indicarlo claramente y generar los escenarios
                necesarios para cubrir la historia actual.

                ==================================================
                6. RECOMENDACIONES
                ==================================================

                Genera recomendaciones concretas para mejorar la suite.

                Evita recomendaciones genéricas.

                ==================================================
                REGLAS IMPORTANTES
                ==================================================

                - No inventes requisitos que contradigan la historia.
                - No elimines casos automáticamente.
                - No cambies los IDs existentes.
                - No supongas que un caso es relevante solamente
                  porque comparte palabras con la historia.
                - Evalúa semánticamente el comportamiento.
                - Debes analizar TODOS los criterios.
                - Debes analizar TODOS los casos.
                - Si la suite pertenece a otra funcionalidad,
                  indícalo explícitamente.
                - Si la cobertura es 0%, los escenarios faltantes
                  NO deben ser una lista vacía.
                - No generes casos duplicados entre los escenarios faltantes.
                - Un caso que prueba llamadas de voz NO debe considerarse
                  relevante para una historia de edición o eliminación
                  de mensajes.
                - Si un caso es IRRELEVANT, su riesgo debe ser NONE.
                - Responde únicamente con JSON válido.
                - No utilices Markdown.
                - No agregues texto antes o después del JSON.

                ==================================================
                FORMATO JSON OBLIGATORIO
                ==================================================

                {
                  "duplicateGroups": 0,
                  "findings": [],
                  "criteriaCoverage": [],
                  "caseAssessments": [],
                  "missingScenarios": [],
                  "recommendations": [],
                  "executiveSummary": ""
                }

                Cada finding:

                {
                  "type": "DUPLICATE",
                  "severity": "MEDIUM",
                  "cases": "TC-001 ↔ TC-002",
                  "explanation": "Explicación",
                  "action": "Acción recomendada"
                }

                Cada criterionCoverage:

                {
                  "criterionId": "CA-01",
                  "criterion": "Texto del criterio",
                  "status": "NOT_COVERED",
                  "relatedCases": [],
                  "gap": "Descripción del gap"
                }

                Cada caseAssessment:

                {
                  "caseId": "TC-001",
                  "relevance": "IRRELEVANT",
                  "risk": "NONE",
                  "coveredCriteria": [],
                  "explanation": "El caso pertenece a otra funcionalidad."
                }

                Cada missingScenario:

                {
                  "title": "Editar mensaje propio dentro del período permitido",
                  "criteria": ["CA-01", "CA-02"],
                  "objective": "Validar que un usuario pueda editar su propio mensaje dentro del período permitido.",
                  "steps": "Enviar un mensaje, esperar un tiempo inferior al límite permitido y editarlo.",
                  "expected": "El mensaje se actualiza correctamente para todos los participantes.",
                  "priority": "HIGH",
                  "type": "POSITIVE"
                }

                ==================================================
                HISTORIA DE USUARIO
                ==================================================

                %s

                ==================================================
                CRITERIOS DE ACEPTACIÓN
                ==================================================

                %s

                ==================================================
                CASOS DE PRUEBA EXISTENTES
                ==================================================

                %s
                """.formatted(
                request.userStory(),
                request.acceptanceCriteria(),
                casesJson
        );
    }

    private String extractOutputText(Response response) {

        try {

            JsonNode root =
                    mapper.readTree(
                            mapper.writeValueAsString(response)
                    );

            JsonNode output =
                    root.path("output");

            if (output.isArray()) {

                StringBuilder result =
                        new StringBuilder();

                for (JsonNode item : output) {

                    JsonNode content =
                            item.path("content");

                    if (content.isArray()) {

                        for (JsonNode element : content) {

                            JsonNode text =
                                    element.path("text");

                            if (!text.isMissingNode()
                                    && !text.isNull()) {

                                result.append(
                                        text.asText()
                                );
                            }
                        }
                    }
                }

                if (result.length() > 0) {

                    return cleanJson(
                            result.toString()
                    );
                }
            }

        } catch (Exception e) {

            throw new IllegalStateException(
                    "No fue posible leer la respuesta de OpenAI.",
                    e
            );
        }

        throw new IllegalStateException(
                "OpenAI no devolvió contenido de texto."
        );
    }

    private String cleanJson(String output) {

        String cleaned =
                output.trim();

        if (cleaned.startsWith("```json")) {

            cleaned =
                    cleaned
                            .substring(7)
                            .trim();

        } else if (cleaned.startsWith("```")) {

            cleaned =
                    cleaned
                            .substring(3)
                            .trim();
        }

        if (cleaned.endsWith("```")) {

            cleaned =
                    cleaned
                            .substring(
                                    0,
                                    cleaned.length() - 3
                            )
                            .trim();
        }

        return cleaned;
    }

    private AnalysisResponse parseAnalysisResponse(
            String output,
            List<TestCase> inputCases
    ) {

        try {

            JsonNode json =
                    mapper.readTree(output);

            int totalCases =
                    inputCases == null
                            ? 0
                            : inputCases.size();

            int duplicateGroups =
                    json
                            .path("duplicateGroups")
                            .asInt(0);

            List<AnalysisResponse.Finding> findings =
                    readList(
                            json.path("findings"),
                            AnalysisResponse.Finding.class
                    );

            List<AnalysisResponse.CriterionCoverage> criteriaCoverage =
                    readList(
                            json.path("criteriaCoverage"),
                            AnalysisResponse.CriterionCoverage.class
                    );

            List<AnalysisResponse.CaseAssessment> caseAssessments =
                    readList(
                            json.path("caseAssessments"),
                            AnalysisResponse.CaseAssessment.class
                    );

            List<AnalysisResponse.MissingScenario> detailedMissingScenarios =
                    readList(
                            json.path("missingScenarios"),
                            AnalysisResponse.MissingScenario.class
                    );

            List<String> recommendations =
                    readList(
                            json.path("recommendations"),
                            String.class
                    );

            String executiveSummary =
                    json
                            .path("executiveSummary")
                            .asText(
                                    "Análisis generado por OpenAI."
                            );

            int relevantCases = 0;
            int partiallyRelevantCases = 0;
            int irrelevantCases = 0;
            int highRiskCases = 0;

            for (AnalysisResponse.CaseAssessment assessment
                    : caseAssessments) {

                if ("RELEVANT".equalsIgnoreCase(
                        assessment.relevance())) {

                    relevantCases++;

                } else if ("PARTIALLY_RELEVANT".equalsIgnoreCase(
                        assessment.relevance())) {

                    partiallyRelevantCases++;

                } else if ("IRRELEVANT".equalsIgnoreCase(
                        assessment.relevance())) {

                    irrelevantCases++;
                }

                if ("HIGH".equalsIgnoreCase(
                        assessment.risk())) {

                    highRiskCases++;
                }
            }

            int criteriaTotal =
                    criteriaCoverage.size();

            int criteriaCovered = 0;
            int criteriaPartiallyCovered = 0;
            int criteriaNotCovered = 0;

            for (AnalysisResponse.CriterionCoverage coverage
                    : criteriaCoverage) {

                if ("COVERED".equalsIgnoreCase(
                        coverage.status())) {

                    criteriaCovered++;

                } else if ("PARTIALLY_COVERED".equalsIgnoreCase(
                        coverage.status())) {

                    criteriaPartiallyCovered++;

                } else if ("NOT_COVERED".equalsIgnoreCase(
                        coverage.status())) {

                    criteriaNotCovered++;
                }
            }

            int coveragePercentage =
                    calculateCoveragePercentage(
                            criteriaTotal,
                            criteriaCovered,
                            criteriaPartiallyCovered
                    );

            int traceableCases =
                    relevantCases;

            List<String> missingScenarioTitles =
                    detailedMissingScenarios
                            .stream()
                            .map(
                                    AnalysisResponse.MissingScenario::title
                            )
                            .toList();

            System.out.println();
            System.out.println(
                    "========== MÉTRICAS CALCULADAS =========="
            );
            System.out.println(
                    "Total casos: " + totalCases
            );
            System.out.println(
                    "Duplicados: " + duplicateGroups
            );
            System.out.println(
                    "Relevantes: " + relevantCases
            );
            System.out.println(
                    "Parcialmente relevantes: "
                            + partiallyRelevantCases
            );
            System.out.println(
                    "Irrelevantes: " + irrelevantCases
            );
            System.out.println(
                    "Alto riesgo: " + highRiskCases
            );
            System.out.println(
                    "Criterios totales: " + criteriaTotal
            );
            System.out.println(
                    "Criterios cubiertos: " + criteriaCovered
            );
            System.out.println(
                    "Criterios parcialmente cubiertos: "
                            + criteriaPartiallyCovered
            );
            System.out.println(
                    "Criterios no cubiertos: "
                            + criteriaNotCovered
            );
            System.out.println(
                    "Cobertura: "
                            + coveragePercentage
                            + "%"
            );
            System.out.println(
                    "Escenarios faltantes: "
                            + detailedMissingScenarios.size()
            );
            System.out.println(
                    "========================================="
            );

            return new AnalysisResponse(
                    "OPENAI",
                    totalCases,
                    duplicateGroups,
                    traceableCases,
                    highRiskCases,
                    findings,
                    missingScenarioTitles,
                    recommendations,
                    executiveSummary,
                    relevantCases,
                    partiallyRelevantCases,
                    irrelevantCases,
                    criteriaTotal,
                    criteriaCovered,
                    criteriaPartiallyCovered,
                    criteriaNotCovered,
                    coveragePercentage,
                    criteriaCoverage,
                    caseAssessments,
                    detailedMissingScenarios
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "OpenAI devolvió una respuesta que no pudo convertirse "
                            + "a AnalysisResponse.",
                    e
            );
        }
    }

    private int calculateCoveragePercentage(
            int total,
            int covered,
            int partiallyCovered
    ) {

        if (total == 0) {
            return 0;
        }

        double score =
                (
                        covered
                                + (partiallyCovered * 0.5)
                )
                        / total;

        return (int) Math.round(
                score * 100
        );
    }

    private <T> List<T> readList(
            JsonNode node,
            Class<T> clazz
    ) {

        if (node == null
                || node.isMissingNode()
                || !node.isArray()) {

            return new ArrayList<>();
        }

        try {

            return mapper.convertValue(
                    node,
                    mapper.getTypeFactory()
                            .constructCollectionType(
                                    List.class,
                                    clazz
                            )
            );

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }

    /**
     * Condición para activar OpenAI únicamente cuando:
     *
     * ai.enabled=true
     * ai.mock=false
     */
    static class OpenAiEnabledCondition
            extends AllNestedConditions {

        OpenAiEnabledCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(
                name = "ai.enabled",
                havingValue = "true"
        )
        static class AiEnabled {
        }

        @ConditionalOnProperty(
                name = "ai.mock",
                havingValue = "false"
        )
        static class AiMockDisabled {
        }
    }
}