package com.example.tripagent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ApprovalTextAgent {

    @SystemMessage("""
            你是一个企业差旅审批说明生成 Agent。
            你的任务是根据差旅申请、推荐方案、预算、合规结果，生成一段给审批人看的审批说明。
            要求：语言正式、简洁，说明出差背景、推荐方案、费用、合规情况和审批建议。
            """)
    @UserMessage("""
            请根据以下信息生成审批说明：

            员工姓名：{{employeeName}}
            部门：{{department}}
            差旅等级：{{travelLevel}}

            出发城市：{{departureCity}}
            目的城市：{{destinationCity}}
            出差天数：{{travelDays}}
            出差事由：{{reason}}
            项目名称：{{projectName}}
            申请预算：{{budgetAmount}}

            推荐方案名称：{{planName}}
            交通费用：{{transportAmount}}
            酒店费用：{{hotelAmount}}
            餐补费用：{{mealAmount}}
            市内交通费用：{{localTransportAmount}}
            方案总费用：{{totalAmount}}

            合规状态：{{complianceStatus}}
            风险说明：{{riskSummary}}
            推荐理由：{{recommendReason}}
            """)
    String generateApprovalText(@V("employeeName") String employeeName,
                                @V("department") String department,
                                @V("travelLevel") String travelLevel,
                                @V("departureCity") String departureCity,
                                @V("destinationCity") String destinationCity,
                                @V("travelDays") Integer travelDays,
                                @V("reason") String reason,
                                @V("projectName") String projectName,
                                @V("budgetAmount") String budgetAmount,
                                @V("planName") String planName,
                                @V("transportAmount") String transportAmount,
                                @V("hotelAmount") String hotelAmount,
                                @V("mealAmount") String mealAmount,
                                @V("localTransportAmount") String localTransportAmount,
                                @V("totalAmount") String totalAmount,
                                @V("complianceStatus") String complianceStatus,
                                @V("riskSummary") String riskSummary,
                                @V("recommendReason") String recommendReason);
}