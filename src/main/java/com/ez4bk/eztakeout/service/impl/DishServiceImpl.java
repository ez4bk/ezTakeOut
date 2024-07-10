package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.dto.DishDto;
import com.ez4bk.eztakeout.entity.Dish;
import com.ez4bk.eztakeout.entity.DishFlavor;
import com.ez4bk.eztakeout.mapper.DishMapper;
import com.ez4bk.eztakeout.service.DishFlavorService;
import com.ez4bk.eztakeout.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // Save the dish
        this.save(dishDto);
        Long dishId = dishDto.getId();
        // Save the flavors
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor -> {
            flavor.setDishId(dishId);
        });
//        flavors.stream().map(item -> {
//            item.setDishId(dishId);
//            return item;
//        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // Update the dish, pass the dishDto directly since DishDto extends Dish
        this.updateById(dishDto);

        // Clear the flavors of the dish
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // Add the new flavors
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor -> {
            flavor.setDishId(dishDto.getId());
        });
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1);

        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("The dish is in use and cannot be deleted.");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(lambdaQueryWrapper);
    }
}
