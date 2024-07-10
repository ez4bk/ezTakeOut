package com.ez4bk.eztakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ez4bk.eztakeout.dto.SetmealDto;
import com.ez4bk.eztakeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void updateWithSetmealDish(SetmealDto setmealDto);

    SetmealDto getByIdWithSetmealDish(Long id);

    void removeWithSetmealDish(List<Long> ids);
}
