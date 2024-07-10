package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.common.CustomException;
import com.ez4bk.eztakeout.dto.SetmealDto;
import com.ez4bk.eztakeout.entity.Setmeal;
import com.ez4bk.eztakeout.entity.SetmealDish;
import com.ez4bk.eztakeout.mapper.SetmealMapper;
import com.ez4bk.eztakeout.service.SetmealDishService;
import com.ez4bk.eztakeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.forEach(dish -> {
            dish.setSetmealId(setmealId);
        });
        setmealDishService.saveBatch(dishes);
    }

    @Override
    @Transactional
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(dish -> {
            dish.setSetmealId(setmealDto.getId());
        });
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getByIdWithSetmealDish(Long id) {
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    @Transactional
    public void removeWithSetmealDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("The dish is in use and cannot be deleted.");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
