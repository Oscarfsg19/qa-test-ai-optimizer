package com.acme.qa.controller;

import com.acme.qa.model.AnalysisRequest;
import com.acme.qa.model.AnalysisResponse;
import com.acme.qa.service.AiQaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QaController {
    private final AiQaService service;

    public QaController(AiQaService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public AnalysisResponse analyze(@Valid @RequestBody AnalysisRequest request) {
        return service.analyze(request);
    }
}
