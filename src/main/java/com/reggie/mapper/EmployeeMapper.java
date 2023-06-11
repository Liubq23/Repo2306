package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 刘秉奇
 * @version 1.0
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
