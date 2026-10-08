package com.acme.qa.service;

import com.acme.qa.model.AnalysisRequest;
import com.acme.qa.model.AnalysisResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiQaService {

    private final LocalQaAnalyzer localAnalyzer;
    private final ObjectProvider<OpenAiQaAnalyzer> openAiAnalyzerProvider;

    @Value("${ai.enabled:false}")
    private boolean enabled;

    @Value("${ai.mock:false}")
    private boolean mock;

    public AiQaService(
            LocalQaAnalyzer localAnalyzer,
            ObjectProvider<OpenAiQaAnalyzer> openAiAnalyzerProvider
    ) {
        this.localAnalyzer = localAnalyzer;
        this.openAiAnalyzerProvider = openAiAnalyzerProvider;
    }

    public AnalysisResponse analyze(AnalysisRequest request) {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("============= AI QA SERVICE ==================");
        System.out.println("ai.enabled = " + enabled);
        System.out.println("ai.mock    = " + mock);
        System.out.println("==============================================");

        // ==========================================
        // MODO LOCAL
        // ==========================================

        if (!enabled) {

            System.out.println(
                    ">>> Usando LocalQaAnalyzer porque ai.enabled=false"
            );

            return localAnalyzer.analyze(
                    request.userStory(),
                    request.acceptanceCriteria(),
                    request.testCases()
            );
        }

        // ==========================================
        // MODO MOCK
        // ==========================================

        if (mock) {

            System.out.println(
                    ">>> Usando MOCK AI - no se realiza llamada a OpenAI"
            );

            return buildMockResponse(request);
        }

        // ==========================================
        // MODO OPENAI REAL
        // ==========================================

        System.out.println(
                ">>> Usando OpenAiQaAnalyzer"
        );

        OpenAiQaAnalyzer openAiAnalyzer =
                openAiAnalyzerProvider.getIfAvailable();

        if (openAiAnalyzer == null) {

            System.out.println(
                    ">>> OpenAiQaAnalyzer no disponible"
            );

            return localAnalyzer.analyze(
                    request.userStory(),
                    request.acceptanceCriteria(),
                    request.testCases()
            );
        }

        try {

            return openAiAnalyzer.analyze(request);

        } catch (Exception ex) {

            System.err.println(
                    ">>> ERROR EN OPENAI: " + ex.getMessage()
            );

            ex.printStackTrace();

            System.out.println(
                    ">>> Usando LocalQaAnalyzer como fallback"
            );

            return localAnalyzer.analyze(
                    request.userStory(),
                    request.acceptanceCriteria(),
                    request.testCases()
            );
        }
    }

    private AnalysisResponse buildMockResponse(
            AnalysisRequest request
    ) {

        int totalCases = request.testCases() == null
                ? 0
                : request.testCases().size();

        List<AnalysisResponse.CriterionCoverage> criteriaCoverage =
                List.of(

                        new AnalysisResponse.CriterionCoverage(
                                "AC-01",
                                "Solo se pueden editar mensajes enviados por el propio usuario.",
                                "COVERED",
                                List.of("TC-MSG-001"),
                                ""
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-02",
                                "La edición solo está permitida durante los primeros 15 minutos.",
                                "PARTIALLY_COVERED",
                                List.of("TC-MSG-002"),
                                "Falta validar explícitamente el límite exacto de 15 minutos."
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-03",
                                "Todos los participantes deben visualizar el contenido actualizado.",
                                "COVERED",
                                List.of("TC-MSG-003"),
                                ""
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-04",
                                "La edición debe sincronizarse entre sesiones y dispositivos.",
                                "NOT_COVERED",
                                List.of(),
                                "No existe un caso que valide sincronización entre múltiples dispositivos."
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-05",
                                "Una edición fuera del periodo permitido debe ser rechazada.",
                                "COVERED",
                                List.of("TC-MSG-005"),
                                ""
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-06",
                                "El usuario puede eliminar un mensaje dentro del periodo permitido.",
                                "COVERED",
                                List.of("TC-MSG-006"),
                                ""
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-07",
                                "Los participantes deben visualizar que el mensaje fue eliminado.",
                                "PARTIALLY_COVERED",
                                List.of("TC-MSG-007"),
                                "Debe validarse la representación exacta del mensaje eliminado."
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-08",
                                "Las modificaciones concurrentes deben producir un estado consistente.",
                                "NOT_COVERED",
                                List.of(),
                                "Falta cobertura de concurrencia."
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-09",
                                "Si se pierde temporalmente la conexión, debe informarse el resultado de la operación.",
                                "NOT_COVERED",
                                List.of(),
                                "Falta cobertura de pérdida y recuperación de conexión."
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-10",
                                "Las modificaciones deben conservar información de auditoría.",
                                "NOT_COVERED",
                                List.of(),
                                "No existe un caso que valide trazabilidad histórica."
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-11",
                                "No se pueden modificar mensajes pertenecientes a otro usuario.",
                                "COVERED",
                                List.of("TC-MSG-011"),
                                ""
                        ),

                        new AnalysisResponse.CriterionCoverage(
                                "AC-12",
                                "Las notificaciones deben mantenerse consistentes después de editar o eliminar.",
                                "NOT_COVERED",
                                List.of(),
                                "Falta validar el comportamiento de las notificaciones."
                        )
                );

        List<AnalysisResponse.CaseAssessment> caseAssessments =
                List.of(

                        new AnalysisResponse.CaseAssessment(
                                "TC-MSG-001",
                                "RELEVANT",
                                "LOW",
                                List.of("AC-01"),
                                "Valida que un usuario pueda editar un mensaje propio."
                        ),

                        new AnalysisResponse.CaseAssessment(
                                "TC-MSG-002",
                                "RELEVANT",
                                "MEDIUM",
                                List.of("AC-02"),
                                "Valida el periodo permitido para editar."
                        ),

                        new AnalysisResponse.CaseAssessment(
                                "TC-MSG-003",
                                "RELEVANT",
                                "MEDIUM",
                                List.of("AC-03"),
                                "Valida la actualización visible para los participantes."
                        ),

                        new AnalysisResponse.CaseAssessment(
                                "TC-MSG-004",
                                "IRRELEVANT",
                                "NONE",
                                List.of(),
                                "El caso no está relacionado con edición o eliminación de mensajes."
                        )
                );

        List<AnalysisResponse.MissingScenario> missingScenarios =
                List.of(

                        new AnalysisResponse.MissingScenario(
                                "Sincronización entre dispositivos",
                                List.of("AC-04"),
                                "Validar que una edición realizada desde un dispositivo se refleje correctamente en otro.",
                                "1. Enviar un mensaje.\n2. Abrir la conversación desde otro dispositivo.\n3. Editar el mensaje desde el primer dispositivo.\n4. Revisar el segundo dispositivo.",
                                "El segundo dispositivo muestra inmediatamente el contenido actualizado y la indicación de edición.",
                                "HIGH",
                                "SYNCHRONIZATION"
                        ),

                        new AnalysisResponse.MissingScenario(
                                "Edición concurrente",
                                List.of("AC-08"),
                                "Validar el comportamiento cuando dos dispositivos intentan modificar el mismo mensaje.",
                                "1. Abrir el mismo mensaje en dos dispositivos.\n2. Modificarlo simultáneamente.\n3. Esperar la sincronización.",
                                "Todos los clientes terminan mostrando un estado consistente.",
                                "HIGH",
                                "CONCURRENCY"
                        ),

                        new AnalysisResponse.MissingScenario(
                                "Pérdida de conexión durante edición",
                                List.of("AC-09"),
                                "Validar el comportamiento ante pérdida temporal de conectividad.",
                                "1. Iniciar una edición.\n2. Interrumpir la conexión.\n3. Completar o cancelar la operación.\n4. Recuperar conexión.",
                                "El usuario recibe información clara sobre si la operación fue aplicada o rechazada.",
                                "HIGH",
                                "RESILIENCE"
                        ),

                        new AnalysisResponse.MissingScenario(
                                "Auditoría de modificaciones",
                                List.of("AC-10"),
                                "Validar que las modificaciones queden registradas.",
                                "1. Enviar un mensaje.\n2. Editarlo.\n3. Consultar el historial de auditoría.",
                                "La auditoría conserva información suficiente sobre la modificación realizada.",
                                "MEDIUM",
                                "AUDIT"
                        ),

                        new AnalysisResponse.MissingScenario(
                                "Consistencia de notificaciones",
                                List.of("AC-12"),
                                "Validar que las notificaciones correspondan al estado actualizado del mensaje.",
                                "1. Enviar un mensaje.\n2. Editarlo o eliminarlo.\n3. Revisar las notificaciones.",
                                "Las notificaciones reflejan correctamente el estado final del mensaje.",
                                "MEDIUM",
                                "NOTIFICATION"
                        )
                );

        List<AnalysisResponse.Finding> findings =
                List.of(

                        new AnalysisResponse.Finding(
                                "IRRELEVANT_CASES",
                                "HIGH",
                                "TC-MSG-004",
                                "Se detectaron casos que no están relacionados con la historia de usuario.",
                                "Revisar y retirar o reemplazar los casos irrelevantes."
                        ),

                        new AnalysisResponse.Finding(
                                "MISSING_COVERAGE",
                                "HIGH",
                                "AC-04, AC-08, AC-09",
                                "Existen criterios importantes sin cobertura suficiente.",
                                "Agregar escenarios de sincronización, concurrencia y resiliencia."
                        ),

                        new AnalysisResponse.Finding(
                                "AUDITABILITY",
                                "MEDIUM",
                                "AC-10",
                                "La trazabilidad de las modificaciones no está cubierta.",
                                "Agregar pruebas de auditoría."
                        )
                );

        List<String> recommendations =
                List.of(
                        "Agregar pruebas de sincronización entre múltiples dispositivos.",
                        "Agregar pruebas de concurrencia.",
                        "Agregar escenarios de pérdida y recuperación de conexión.",
                        "Validar la información de auditoría de las modificaciones.",
                        "Validar consistencia de las notificaciones.",
                        "Eliminar o reemplazar casos de prueba que no correspondan a la historia de usuario."
                );

        int covered = 5;
        int partiallyCovered = 2;
        int notCovered = 5;

        int coveragePercentage =
                (int) Math.round(
                        ((covered + (partiallyCovered * 0.5))
                                / 12.0) * 100
                );

        return new AnalysisResponse(
                "OPENAI_MOCK",
                totalCases,
                2,
                3,
                3,
                findings,
                missingScenarios.stream()
                        .map(AnalysisResponse.MissingScenario::title)
                        .toList(),
                recommendations,
                "El análisis simulado identifica cobertura parcial de los criterios de aceptación y varios escenarios relevantes que deben incorporarse a la suite de pruebas.",
                3,
                1,
                Math.max(0, totalCases - 4),
                12,
                covered,
                partiallyCovered,
                notCovered,
                coveragePercentage,
                criteriaCoverage,
                caseAssessments,
                missingScenarios
        );
    }
}