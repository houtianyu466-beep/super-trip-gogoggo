package com.example.tripagent.config;

import com.example.tripagent.agent.RequirementParseAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequirementParseAgentConfig {

    @Bean
    public RequirementParseAgent requirementParseAgent(ChatModel chatModel) {
        return AiServices.builder(RequirementParseAgent.class)
                .chatModel(chatModel)
                .build();
    }
}