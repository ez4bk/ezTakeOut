package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.common.CustomException;
import com.ez4bk.eztakeout.entity.Category;
import com.ez4bk.eztakeout.entity.Dish;
import com.ez4bk.eztakeout.entity.Setmeal;
import com.ez4bk.eztakeout.mapper.CategoryMapper;
import com.ez4bk.eztakeout.service.CategoryService;
import com.ez4bk.eztakeout.service.DishService;
import com.ez4bk.eztakeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setMealService;

    /**
     * Remove category by id
     * Check if the category is used by dish or set meal
     *
     * @param id category id to be removed
     */
    public void removeByIdSafe(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        long dishCount = dishService.count(dishQueryWrapper);
        if (dishCount > 0) {
            throw new CustomException("Category is used by dish");
        }

        LambdaQueryWrapper<Setmeal> setMealQueryWrapper = new LambdaQueryWrapper<>();
        setMealQueryWrapper.eq(Setmeal::getCategoryId, id);
        long setMealCount = setMealService.count(setMealQueryWrapper);
        if (setMealCount > 0) {
            throw new CustomException("Category is used by set meal");
        }
        super.removeById(id);

    }
}
