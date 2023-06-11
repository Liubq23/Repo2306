package com.reggie.entity;


import lombok.Data;

import java.util.List;

/**
 * @author 刘秉奇
 * @version 1.0
 */

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
}
