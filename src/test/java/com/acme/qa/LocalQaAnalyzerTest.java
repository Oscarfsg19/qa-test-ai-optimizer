package com.acme.qa;

import com.acme.qa.model.TestCase;
import com.acme.qa.service.LocalQaAnalyzer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LocalQaAnalyzerTest {
    @Test
    void detectsPotentialDuplicate() {
        var analyzer = new LocalQaAnalyzer();
        var cases = List.of(
                new TestCase("TC-1","Send message","write and send","message arrives","HIGH"),
                new TestCase("TC-2","Send message","write and send","message arrives","MEDIUM")
        );
        var result = analyzer.analyze("Send messages", "Message arrives", cases);
        assertTrue(result.duplicateGroups() >= 1);
    }
}
