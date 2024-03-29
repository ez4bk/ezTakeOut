package com.ez4bk.eztakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.entity.Category;
import com.ez4bk.eztakeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
