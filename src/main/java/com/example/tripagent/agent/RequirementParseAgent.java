package com.example.tripagent.agent;

import com.example.tripagent.domain.dto.ParsedTravelRequirement;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface RequirementParseAgent {

    @SystemMessage("""
            你是一个企业差旅需求解析 Agent。
            你的任务是把用户的自然语言出差需求解析成结构化字段。

            当前日期是 2026-06-22，时区是 Asia/Shanghai。
            如果用户说“明天”“下周一”等相对日期，请基于当前日期解析。

            规则：
            1. 只提取用户明确表达或可以合理推断的字段
            2. 不要编造出发地、目的地、预算、事由
            3. 如果字段缺失，保持 null
            4. needTransport 和 needHotel 如果用户没有明确拒绝，默认 true
            5. reason 应该简洁概括出差目的
            """)
    ParsedTravelRequirement parse(@UserMessage String message);
}