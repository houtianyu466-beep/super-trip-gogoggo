package com.example.tripagent.agent;

import com.example.tripagent.domain.dto.ParsedTravelRequirement;

public interface RequirementParseAgent {
    ParsedTravelRequirement parse(String message);
}
