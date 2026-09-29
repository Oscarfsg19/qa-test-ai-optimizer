package com.acme.qa.model;

public record TestCase(
        String id,
        String title,
        String steps,
        String expected,
        String priority
) {}
