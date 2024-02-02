package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.entity.Employee;
import com.ez4bk.eztakeout.mapper.EmployeeMapper;
import com.ez4bk.eztakeout.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
