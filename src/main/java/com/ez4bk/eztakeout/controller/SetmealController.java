package com.ez4bk.eztakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.dto.DishDto;
import com.ez4bk.eztakeout.dto.SetmealDto;
import com.ez4bk.eztakeout.entity.Category;
import com.ez4bk.eztakeout.entity.Setmeal;
import com.ez4bk.eztakeout.service.CategoryService;
import com.ez4bk.eztakeout.service.SetmealDishService;
import com.ez4bk.eztakeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        log.info("Add setmeal: {}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("Add setmeal successfully");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> pageSetmeal(int page, int pageSize, String name) {
        log.info("Setmeal list paging: page={}, pageSize={}", page, pageSize);
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        log.info("Get setmeal by id: id={}", id);
        DishDto dishDto = setmealService.getBy(id);
        return R.success(dishDto);
    }

}
