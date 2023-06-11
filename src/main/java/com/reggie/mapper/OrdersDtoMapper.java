package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.OrdersDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 刘秉奇
 * @version 1.0
 */
@Mapper
public interface OrdersDtoMapper extends BaseMapper<OrdersDto> {
}
