package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.entity.Category;
import com.reggie.entity.Dish;
import com.reggie.entity.Setmeal;
import com.reggie.mapper.CategoryMapper;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    @Transactional
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        //是否关联菜品，有关联抛出业务异常
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            //有关联，抛出异常
            throw new CustomException("当前分类有关联菜品，不能删除");
        }

        //是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);

        //是否关联菜品，有关联抛出业务异常
        int count2 = dishService.count(dishLambdaQueryWrapper);
        if (count2 > 0) {
            //有关联套餐，抛出异常
            throw new CustomException("当前分类有关联套餐，不能删除");
        }


        //正常删除
        super.removeById(id);
    }
}