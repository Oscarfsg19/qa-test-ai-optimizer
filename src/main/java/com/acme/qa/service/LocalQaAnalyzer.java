package com.acme.qa.service;

import com.acme.qa.model.AnalysisResponse;
import com.acme.qa.model.TestCase;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocalQaAnalyzer {

    public AnalysisResponse analyze(String story, String criteria, List<TestCase> input) {
        List<TestCase> cases = input == null ? List.of() : input;
        List<AnalysisResponse.Finding> findings = new ArrayList<>();

        int highRisk = 0;
        int traceable = 0;

        for (TestCase tc : cases) {
            String text = normalize(tc.title() + " " + tc.steps() + " " + tc.expected());
            if (containsAny(text, "send", "deliver", "message", "recipient", "error", "offline", "attachment")) {
                traceable++;
            }
            if ("HIGH".equalsIgnoreCase(tc.priority()) || containsAny(text, "security", "duplicate", "offline", "failure", "error")) {
                highRisk++;
            }
        }

        // Pairwise similarity: intentionally simple and explainable for a demo.
        Set<String> seen = new HashSet<>();
        int duplicateGroups = 0;
        for (int i = 0; i < cases.size(); i++) {
            if (seen.contains(cases.get(i).id())) continue;
            List<String> dupIds = new ArrayList<>();
            for (int j = i + 1; j < cases.size(); j++) {
                double sim = similarity(cases.get(i), cases.get(j));
                if (sim >= 0.78) dupIds.add(cases.get(j).id());
            }
            if (!dupIds.isEmpty()) {
                duplicateGroups++;
                seen.add(cases.get(i).id());
                seen.addAll(dupIds);
                findings.add(new AnalysisResponse.Finding(
                        "DUPLICATE",
                        "MEDIUM",
                        cases.get(i).id() + " ↔ " + String.join(", ", dupIds),
                        "Los casos comparten pasos, objetivo y resultado esperado con alta similitud.",
                        "Revisar y consolidar en un caso parametrizable."
                ));
            }
        }

        List<String> missing = inferMissingScenarios(story + "\n" + criteria, cases);

        if (cases.isEmpty()) {
            findings.add(new AnalysisResponse.Finding(
                    "COVERAGE",
                    "HIGH",
                    "—",
                    "No hay casos de prueba cargados para el requisito analizado.",
                    "Crear una baseline de smoke/regression antes del próximo deploy."
            ));
        }

        if (missing.size() >= 2) {
            findings.add(new AnalysisResponse.Finding(
                    "MISSING_COVERAGE",
                    "HIGH",
                    "—",
                    "Se detectaron escenarios funcionales relevantes no representados claramente en la suite.",
                    "Agregar casos y vincularlos a criterios de aceptación."
            ));
        }

        List<String> recommendations = List.of(
                "Mantener un identificador único por caso y una relación explícita con historia/criterio.",
                "Consolidar duplicados antes de aumentar el volumen de automatización.",
                "Automatizar primero smoke y regresiones de alto riesgo; conservar exploración manual para escenarios ambiguos.",
                "Usar la IA como recomendador: cualquier eliminación o cambio de cobertura requiere validación de QA."
        );

        String summary = String.format(
                "La suite contiene %d casos. El análisis identificó %d grupo(s) potencialmente duplicado(s), %d casos con trazabilidad funcional clara y %d casos de alto riesgo. Se proponen %d escenarios adicionales.",
                cases.size(), duplicateGroups, traceable, highRisk, missing.size()
        );

        return new AnalysisResponse(
                "LOCAL_HEURISTIC",
                cases.size(),
                duplicateGroups,
                traceable,
                highRisk,
                findings,
                missing,
                recommendations,
                summary
        );
    }

    private List<String> inferMissingScenarios(String text, List<TestCase> cases) {
        String normalizedCases = cases.stream()
                .map(tc -> normalize(tc.title() + " " + tc.steps() + " " + tc.expected()))
                .collect(Collectors.joining(" "));

        List<String> candidates = new ArrayList<>();
        Map<String, String> scenarioMap = new LinkedHashMap<>();
        scenarioMap.put("offline", "Enviar mensaje cuando el emisor está temporalmente offline y verificar reintento/estado.");
        scenarioMap.put("duplicate", "Evitar el envío duplicado ante doble clic, retry o timeout.");
        scenarioMap.put("order", "Verificar que los mensajes mantengan el orden esperado en conversaciones concurrentes.");
        scenarioMap.put("recipient", "Validar destinatario inválido/no disponible y el mensaje de error.");
        scenarioMap.put("large", "Validar comportamiento con contenido grande o cercano al límite permitido.");
        scenarioMap.put("permission", "Validar permisos/autorización para la operación.");

        for (var e : scenarioMap.entrySet()) {
            if (normalize(text).contains(e.getKey()) && !normalizedCases.contains(e.getKey())) {
                candidates.add(e.getValue());
            }
        }

        if (candidates.isEmpty() && cases.size() < 3) {
            candidates.add("Agregar caso negativo explícito para errores de validación o de backend.");
            candidates.add("Agregar caso de recuperación ante timeout/reintento.");
        }

        return candidates;
    }

    private double similarity(TestCase a, TestCase b) {
        Set<String> x = tokens(a.title() + " " + a.steps() + " " + a.expected());
        Set<String> y = tokens(b.title() + " " + b.steps() + " " + b.expected());
        if (x.isEmpty() || y.isEmpty()) return 0;
        Set<String> intersection = new HashSet<>(x);
        intersection.retainAll(y);
        Set<String> union = new HashSet<>(x);
        union.addAll(y);
        return (double) intersection.size() / union.size();
    }

    private Set<String> tokens(String value) {
        return Arrays.stream(normalize(value).split("\\s+"))
                .filter(s -> s.length() > 3)
                .collect(Collectors.toSet());
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{Nd}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean containsAny(String text, String... terms) {
        for (String t : terms) if (text.contains(t)) return true;
        return false;
    }
}
