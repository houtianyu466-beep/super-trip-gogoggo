package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.common.enums.ResponseCodeEnum;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.domain.entity.Employee;
import com.example.tripagent.mapper.EmployeeMapper;
import com.example.tripagent.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl
        extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @Override
    public Employee getActiveEmployeeById(Long employeeId) {
        if(employeeId == null){
            throw new BusinessException(ResponseCodeEnum.PARAM_ERROR);
        }
        Employee employee = getById(employeeId);
        if (!"ACTIVE".equals(employee.getStatus())) {
            throw new BusinessException("员工状态不可用");
        }
        return employee;
    }
}
