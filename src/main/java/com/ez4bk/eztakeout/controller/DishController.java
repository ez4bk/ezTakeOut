package com.ez4bk.eztakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.dto.DishDto;
import com.ez4bk.eztakeout.entity.Category;
import com.ez4bk.eztakeout.entity.Dish;
import com.ez4bk.eztakeout.service.CategoryService;
import com.ez4bk.eztakeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        log.info("Add dish: {}", dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("Add dish successfully");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> pageDish(int page, int pageSize, String name) {
        log.info("Dish list paging: page={}, pageSize={}", page, pageSize);
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);

    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        log.info("Get dish by id: id={}", id);
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        log.info("Update dish: {}", dishDto.getName());
        dishService.updateWithFlavor(dishDto);
        return R.success("Update dish successfully");
    }

    @GetMapping("/list")
    public R<List<Dish>> listDish(Dish dish) {
        log.info("Dish list: {}", dish);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }
}
