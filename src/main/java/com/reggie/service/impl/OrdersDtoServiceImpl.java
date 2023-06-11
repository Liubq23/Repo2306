package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.entity.OrdersDto;
import com.reggie.mapper.OrdersDtoMapper;
import com.reggie.service.OrdersDtoService;
import org.springframework.stereotype.Service;

/**
 * @author 刘秉奇
 * @version 1.0
 */
@Service
public class OrdersDtoServiceImpl extends ServiceImpl<OrdersDtoMapper, OrdersDto> implements OrdersDtoService {
}
