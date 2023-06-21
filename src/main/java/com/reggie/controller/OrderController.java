package com.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.entity.OrderDetail;
import com.reggie.entity.Orders;
import com.reggie.entity.OrdersDto;
import com.reggie.service.OrderDetailService;
import com.reggie.service.OrderService;
import com.reggie.service.OrdersDtoService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 刘秉奇
 * @version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;


    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return Result.success("下单成功");
    }


    @GetMapping("/page")
    public Result<Page> list(int page, int pageSize, Long orderId, String beginTime, String endTime){
        Page pageInfo = new Page(page, pageSize);
        Page pageInfoDto = new Page(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orderId != null, Orders::getId, orderId);

        queryWrapper.gt(!StringUtils.isEmpty(beginTime), Orders::getOrderTime, beginTime)
                .lt(!StringUtils.isEmpty(endTime), Orders::getOrderTime, endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo, queryWrapper);

        //用ordersDto保存orderDetail

        //orders复制过去
        BeanUtils.copyProperties(pageInfo, pageInfoDto, "records");
        List<Orders> records = pageInfo.getRecords();
        //遍历加入OrderDetail
        List<OrdersDto> list = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(item, ordersDto);

            //根据orderId设置orderDetail
            Long orderId1 = item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId, orderId1);
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper1);
            ordersDto.setOrderDetails(orderDetails);

            return ordersDto;
        }).collect(Collectors.toList());
        pageInfoDto.setRecords(list);
        log.info("list:{}",list);
        return Result.success(pageInfoDto);
    }


    @GetMapping("/userPage")
    public Result<Page> orderList(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);
        Page pageInfoDto = new Page(page, pageSize);

        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        //用ordersDto保存orderDetail

        //orders复制过去
        BeanUtils.copyProperties(pageInfo, pageInfoDto, "records");
        List<Orders> records = pageInfo.getRecords();
        //遍历加入OrderDetail
        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(item, ordersDto);

            //根据orderId设置orderDetail
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper1);
            ordersDto.setOrderDetails(orderDetails);

            return ordersDto;
        }).collect(Collectors.toList());
        pageInfoDto.setRecords(list);
        log.info("list:{}", list);
        return Result.success(pageInfoDto);
    }

    @PutMapping
    public Result<String> updateOrder(@RequestBody Map<String, String> map){
        Long orderId = Long.valueOf(map.get("id"));
        Integer status = Integer.valueOf(map.get("status"));

        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId, orderId);
        updateWrapper.set(Orders::getStatus,status);

        orderService.update(updateWrapper);

        return Result.success("订单状态修改成功");
    }

}
