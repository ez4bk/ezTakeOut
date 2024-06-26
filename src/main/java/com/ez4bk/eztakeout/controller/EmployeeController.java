package com.ez4bk.eztakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.entity.Employee;
import com.ez4bk.eztakeout.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> employeeLogin(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee tempEmployee = employeeService.getOne(queryWrapper);

        log.info("User attempt to login: {}", employee.getUsername());

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
        log.info(tempEmployee.getUsername() + " logged in");

        return R.success(tempEmployee);
    }

    @PostMapping("/logout")
    public R<String> employeeLogout(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("employee");
        String username = employeeService.getById(userId).getUsername();
        log.info(username + " logout");
        request.getSession().removeAttribute("employee");
        return R.success("Logout successfully");
    }

    /**
     * Add employee
     *
     * @param employee to be added
     * @return R<String>  Add employee result
     */
    @PostMapping("")
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("Add employee: {}", employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));  // Default password
        employeeService.save(employee);

        return R.success("Add employee successfully");
    }


    /**
     * Employee list paging
     * @param page  Current page
     * @param pageSize  Number of records per page
     * @param name  Employee name to be searched
     * @return Employee list in Page type
     */
    @GetMapping("/page")
    public R<Page<Employee>> pageEmployee(int page, int pageSize, String name) {
        log.info("Employee list paging: page={}, pageSize={}, name={}", page, pageSize, name);
        // Construct page object
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // Construct query conditions
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // Execute search
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * Update employee
     * @param request  Request object
     * @param employee Employee object to be updated
     * @return R<String>  Update employee result
     */
    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("Update employee: {}", employee.toString());
        Long currEmpId = (Long) request.getSession().getAttribute("employee");

        String currEmpName = employeeService.getById(currEmpId).getUsername();
        Employee employeeInDB = employeeService.getById(employee.getId());
        if (!currEmpName.contains("admin") && !Objects.equals(employeeInDB.getStatus(), employee.getStatus())) {
            return R.error("Illegal operation, only admin can modify the employee's status.");
        }
        if (currEmpId.equals(employee.getId())) {
            return R.error("Illegal operation, cannot modify your own information.");
        }
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(currEmpId);
        employeeService.updateById(employee);
        return R.success("Update employee successfully");
    }

    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("Employee does not exist");
        }
        return R.success(employee);
    }
}
