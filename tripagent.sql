create table agent_audit_log
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    request_id    bigint                             null comment '出差申请ID',
    agent_name    varchar(128)                       not null comment 'Agent名称',
    step_name     varchar(128)                       not null comment '执行步骤名称',
    input_text    longtext                           null comment 'Agent输入内容',
    output_text   longtext                           null comment 'Agent输出内容',
    tool_name     varchar(128)                       null comment '调用工具名称',
    tool_input    longtext                           null comment '工具入参JSON',
    tool_output   longtext                           null comment '工具出参JSON',
    success       tinyint  default 1                 not null comment '是否成功：0失败 1成功',
    error_message text                               null comment '错误信息',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment 'Agent审计日志表';

create index idx_agent_name
    on agent_audit_log (agent_name);

create index idx_create_time
    on agent_audit_log (create_time);

create index idx_request_id
    on agent_audit_log (request_id);

create table approval_record
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    request_id       bigint                                not null comment '出差申请ID',
    approver_id      bigint                                null comment '审批人ID',
    approval_level   int         default 1                 not null comment '审批层级',
    approval_status  varchar(32) default 'PENDING'         not null comment '审批状态：PENDING/APPROVED/REJECTED',
    approval_comment varchar(500)                          null comment '审批意见',
    approval_time    datetime                              null comment '审批时间',
    create_time      datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '审批记录表';

create index idx_approval_status
    on approval_record (approval_status);

create index idx_approver_id
    on approval_record (approver_id);

create index idx_request_id
    on approval_record (request_id);

create table business_travel_request
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    request_no       varchar(64)                           not null comment '出差申请单号',
    employee_id      bigint                                not null comment '员工ID',
    departure_city   varchar(64)                           not null comment '出发城市',
    destination_city varchar(64)                           not null comment '目的城市',
    start_date       date                                  null comment '出差开始日期',
    end_date         date                                  null comment '出差结束日期',
    travel_days      int                                   not null comment '出差天数',
    reason           varchar(255)                          not null comment '出差事由',
    project_name     varchar(255)                          null comment '项目或客户名称',
    budget_amount    decimal(10, 2)                        not null comment '申请预算金额',
    need_transport   tinyint     default 1                 not null comment '是否需要交通预订：0否 1是',
    need_hotel       tinyint     default 1                 not null comment '是否需要酒店预订：0否 1是',
    raw_message      text                                  null comment '用户原始自然语言输入',
    parsed_json      text                                  null comment 'Agent解析后的结构化JSON',
    status           varchar(32) default 'DRAFT'           not null comment '状态：DRAFT/GENERATED/SUBMITTED/APPROVED/REJECTED',
    create_time      datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_request_no
        unique (request_no)
)
    comment '企业出差申请表';

create index idx_create_time
    on business_travel_request (create_time);

create index idx_employee_id
    on business_travel_request (employee_id);

create index idx_status
    on business_travel_request (status);

create table compliance_result
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    request_id   bigint                             not null comment '出差申请ID',
    plan_id      bigint                             not null comment '差旅方案ID',
    check_item   varchar(64)                        not null comment '检查项：HOTEL_LIMIT/TRANSPORT_LEVEL/BUDGET_LIMIT/DAYS_REASONABLE',
    check_status varchar(32)                        not null comment '检查结果：PASS/WARNING/FAIL',
    actual_value varchar(255)                       null comment '实际值',
    limit_value  varchar(255)                       null comment '限制值',
    message      varchar(500)                       null comment '校验说明',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '合规校验结果表';

create index idx_check_status
    on compliance_result (check_status);

create index idx_plan_id
    on compliance_result (plan_id);

create index idx_request_id
    on compliance_result (request_id);

create table employee
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    employee_no  varchar(64)                           not null comment '员工编号',
    name         varchar(64)                           not null comment '员工姓名',
    department   varchar(128)                          not null comment '部门名称',
    job_title    varchar(128)                          not null comment '职位名称',
    travel_level varchar(32)                           not null comment '差旅等级：STAFF/MANAGER/DIRECTOR',
    status       varchar(32) default 'ACTIVE'          not null comment '员工状态：ACTIVE/DISABLED',
    create_time  datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_employee_no
        unique (employee_no)
)
    comment '员工表';

create index idx_travel_level
    on employee (travel_level);

create table hotel_option
(
    id                 bigint auto_increment comment '主键ID'
        primary key,
    request_id         bigint                             not null comment '出差申请ID',
    hotel_name         varchar(255)                       not null comment '酒店名称',
    city               varchar(64)                        not null comment '城市',
    business_area      varchar(128)                       null comment '商圈或区域',
    price_per_night    decimal(10, 2)                     not null comment '每晚价格',
    nights             int                                not null comment '入住晚数',
    total_price        decimal(10, 2)                     not null comment '酒店总价',
    distance_desc      varchar(255)                       null comment '距离客户或会议地点说明',
    is_agreement_hotel tinyint  default 0                 not null comment '是否协议酒店：0否 1是',
    rating             decimal(3, 1)                      null comment '评分',
    is_recommended     tinyint  default 0                 not null comment '是否推荐：0否 1是',
    create_time        datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '酒店候选方案表';

create index idx_city
    on hotel_option (city);

create index idx_price_per_night
    on hotel_option (price_per_night);

create index idx_request_id
    on hotel_option (request_id);

create table transport_option
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    request_id       bigint                             not null comment '出差申请ID',
    transport_type   varchar(32)                        not null comment '交通类型：TRAIN/FLIGHT',
    seat_level       varchar(64)                        not null comment '座位等级：SECOND_CLASS/FIRST_CLASS/ECONOMY/BUSINESS',
    departure_city   varchar(64)                        not null comment '出发城市',
    arrival_city     varchar(64)                        not null comment '到达城市',
    departure_time   datetime                           null comment '出发时间',
    arrival_time     datetime                           null comment '到达时间',
    price            decimal(10, 2)                     not null comment '价格',
    duration_minutes int                                null comment '耗时分钟',
    provider         varchar(128)                       null comment '车次或航班号',
    is_recommended   tinyint  default 0                 not null comment '是否推荐：0否 1是',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '交通候选方案表';

create index idx_price
    on transport_option (price);

create index idx_request_id
    on transport_option (request_id);

create table travel_plan
(
    id                     bigint auto_increment comment '主键ID'
        primary key,
    request_id             bigint                                   not null comment '出差申请ID',
    plan_name              varchar(128)                             not null comment '方案名称',
    transport_option_id    bigint                                   null comment '交通方案ID',
    hotel_option_id        bigint                                   null comment '酒店方案ID',
    transport_amount       decimal(10, 2) default 0.00              not null comment '交通费用',
    hotel_amount           decimal(10, 2) default 0.00              not null comment '酒店费用',
    meal_amount            decimal(10, 2) default 0.00              not null comment '餐补费用',
    local_transport_amount decimal(10, 2) default 0.00              not null comment '市内交通费用',
    total_amount           decimal(10, 2)                           not null comment '方案总费用',
    compliance_status      varchar(32)                              not null comment '合规状态：PASS/WARNING/REJECT',
    score                  int            default 0                 not null comment '方案评分',
    risk_summary           text                                     null comment '风险说明',
    recommend_reason       text                                     null comment '推荐理由',
    approval_text          text                                     null comment '审批说明',
    create_time            datetime       default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '差旅方案表';

create index idx_compliance_status
    on travel_plan (compliance_status);

create index idx_request_id
    on travel_plan (request_id);

create index idx_score
    on travel_plan (score);

create table travel_policy
(
    id                       bigint auto_increment comment '主键ID'
        primary key,
    travel_level             varchar(32)                        not null comment '差旅等级：STAFF/MANAGER/DIRECTOR',
    hotel_limit_per_night    decimal(10, 2)                     not null comment '每晚酒店报销上限',
    transport_level          varchar(128)                       not null comment '允许交通标准，如高铁二等座/飞机经济舱',
    meal_allowance_per_day   decimal(10, 2)                     not null comment '每日餐补标准',
    local_transport_per_day  decimal(10, 2)                     not null comment '每日市内交通补贴',
    approval_required_amount decimal(10, 2)                     not null comment '超过该金额需要审批',
    description              text                               null comment '政策说明',
    create_time              datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time              datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_travel_level
        unique (travel_level)
)
    comment '差旅政策表';

