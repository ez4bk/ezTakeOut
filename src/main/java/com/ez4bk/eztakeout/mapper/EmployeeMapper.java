package com.ez4bk.eztakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ez4bk.eztakeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
