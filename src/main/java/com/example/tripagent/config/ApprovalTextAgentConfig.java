package com.example.tripagent.config;

import com.example.tripagent.agent.ApprovalTextAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApprovalTextAgentConfig {

    @Bean
    public ApprovalTextAgent approvalTextAgent(ChatModel chatModel) {
        return AiServices.builder(ApprovalTextAgent.class)
                .chatModel(chatModel)
                .build();
    }
}