package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Employee getActiveEmployeeById(Long employeeId);
}
