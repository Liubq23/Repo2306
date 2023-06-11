package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;


public interface DishService extends IService<Dish> {

    //操作dish和dish_flavor两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品和口味信息
    public DishDto getIdWithFlavor(Long id);

    //更新菜品和口味信息
    public void updateWithFlavor(DishDto dishDto);
}
