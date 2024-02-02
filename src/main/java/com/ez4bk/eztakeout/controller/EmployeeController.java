package com.ez4bk.eztakeout.controller;

import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.entity.Employee;
import com.ez4bk.eztakeout.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee tempEmployee = employeeService.getOne(queryWrapper);

        if (tempEmployee == null) {
            return R.error("User does not exist");
        }

        if (!tempEmployee.getPassword().equals(password)) {
            return R.error("Password is incorrect");
        }

        if (tempEmployee.getStatus() == 0) {
            return R.error("User is disabled");
        }

        request.getSession().setAttribute("employee", tempEmployee.getId());
        return R.success(tempEmployee);
    }
}
