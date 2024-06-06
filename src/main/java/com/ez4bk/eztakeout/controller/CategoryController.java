package com.ez4bk.eztakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.entity.Category;
import com.ez4bk.eztakeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        log.info("Add category: {}", category.getName());

        categoryService.save(category);

        return R.success("Add category successfully");
    }

    @GetMapping("/page")
    public R<Page<Category>> pageCategory(int page, int pageSize) {
        log.info("Category list paging: page={}, pageSize={}", page, pageSize);
        Page<Category> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> deleteCategory(Long id) {
        log.info("Delete category: id={}", id);
        categoryService.removeByIdSafe(id);
        return R.success("Delete category successfully");
    }

    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        log.info("Update category: id={}", category.getId());
        categoryService.updateById(category);
        return R.success("Update category successfully");
    }

    /***
     * Get category list by condition
     * @param category dish category
     * @return R return type
     */
    @GetMapping("/list")
    public R<List<Category>> dropDownCategoryList(Category category) {
        log.info("Category list: {}", category);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
