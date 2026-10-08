package com.acme.qa.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        name = "ai.mock",
        havingValue = "false",
        matchIfMissing = false
)
public class OpenAiConfig {

    @Bean
    @ConditionalOnProperty(
            name = "ai.enabled",
            havingValue = "true",
            matchIfMissing = false
    )
    public OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.fromEnv();
    }
}