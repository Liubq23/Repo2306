package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void updatWithDish(SetmealDto setmealDto);

    SetmealDto getSetmealDtobyId(Long id);

    void removeWithDish(List<Long> ids);
}
