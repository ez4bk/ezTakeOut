package com.ez4bk.eztakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ez4bk.eztakeout.dto.DishDto;
import com.ez4bk.eztakeout.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    // When adding a dish, save the dish and its flavors (two tables)

    void saveWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void removeWithFlavor(List<Long> ids);

}
