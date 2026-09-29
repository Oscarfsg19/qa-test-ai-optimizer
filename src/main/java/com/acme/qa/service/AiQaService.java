package com.acme.qa.service;

import com.acme.qa.model.AnalysisRequest;
import com.acme.qa.model.AnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AiQaService {

    private final LocalQaAnalyzer localAnalyzer;
    private final ObjectMapper mapper;
    private final RestClient restClient;

    @Value("${ai.enabled:false}")
    private boolean enabled;

    @Value("${ai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${ai.api-key:}")
    private String apiKey;

    @Value("${ai.model:gpt-5.6-luna}")
    private String model;

    public AiQaService(LocalQaAnalyzer localAnalyzer, ObjectMapper mapper) {
        this.localAnalyzer = localAnalyzer;
        this.mapper = mapper;
        this.restClient = RestClient.builder().build();
    }

    public AnalysisResponse analyze(AnalysisRequest request) {
        if (!enabled || apiKey == null || apiKey.isBlank()) {
            return localAnalyzer.analyze(request.userStory(), request.acceptanceCriteria(), request.testCases());
        }

        try {
            String prompt = buildPrompt(request);
            Map<String, Object> body = Map.of(
                    "model", model,
                    "input", prompt
            );

            String raw = restClient.post()
                    .uri(baseUrl + "/responses")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            JsonNode root = mapper.readTree(raw);
            String output = extractOutputText(root);
            return new AnalysisResponse(
                    "OPENAI_RESPONSES_API",
                    request.testCases() == null ? 0 : request.testCases().size(),
                    0, 0, 0,
                    List.of(new AnalysisResponse.Finding(
                            "AI_REVIEW", "INFO", "ALL",
                            output,
                            "Revisar las recomendaciones con QA antes de modificar la suite."
                    )),
                    List.of(),
                    List.of("La respuesta fue generada por IA; usarla como recomendación y no como decisión automática."),
                    "Análisis generado por IA. Ver detalle en el hallazgo."
            );
        } catch (Exception ex) {
            AnalysisResponse fallback = localAnalyzer.analyze(
                    request.userStory(), request.acceptanceCriteria(), request.testCases());
            return new AnalysisResponse(
                    "LOCAL_FALLBACK",
                    fallback.totalCases(),
                    fallback.duplicateGroups(),
                    fallback.traceableCases(),
                    fallback.highRiskCases(),
                    fallback.findings(),
                    fallback.missingScenarios(),
                    fallback.recommendations(),
                    fallback.executiveSummary() + " La integración IA no respondió; se utilizó el análisis local."
            );
        }
    }

    private String extractOutputText(JsonNode root) {
        JsonNode output = root.path("output");
        if (output.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (JsonNode item : output) {
                JsonNode content = item.path("content");
                if (content.isArray()) {
                    for (JsonNode c : content) {
                        if (c.has("text")) sb.append(c.path("text").asText()).append("\n");
                    }
                }
            }
            if (!sb.isEmpty()) return sb.toString().trim();
        }
        return root.path("output_text").asText("La API no devolvió texto legible.");
    }

    private String buildPrompt(AnalysisRequest r) {
        return """
                Actúa como QA Lead para una plataforma de mensajería empresarial.
                Analiza una suite de pruebas frente a una historia de usuario y sus criterios.
                Objetivo: detectar duplicados, huecos de cobertura, riesgo de regresión y oportunidades
                de consolidación. No elimines pruebas automáticamente. Devuelve una revisión concisa
                con: resumen, duplicados potenciales, casos faltantes, riesgos y recomendaciones.

                HISTORIA:
                %s

                CRITERIOS:
                %s

                CASOS:
                %s
                """.formatted(r.userStory(), r.acceptanceCriteria(),
                mapper.valueToTree(r.testCases() == null ? List.of() : r.testCases()).toPrettyString());
    }
}
