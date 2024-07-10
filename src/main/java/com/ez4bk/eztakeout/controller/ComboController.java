package com.ez4bk.eztakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ez4bk.eztakeout.common.R;
import com.ez4bk.eztakeout.dto.ComboDto;
import com.ez4bk.eztakeout.entity.Category;
import com.ez4bk.eztakeout.entity.Combo;
import com.ez4bk.eztakeout.service.CategoryService;
import com.ez4bk.eztakeout.service.ComboDishService;
import com.ez4bk.eztakeout.service.ComboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/combo")
public class ComboController {

    @Autowired
    private ComboService comboService;
    @Autowired
    private ComboDishService comboDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public R<String> addCombo(@RequestBody ComboDto comboDto) {
        log.info("Add combo: {}", comboDto);
        comboService.saveWithDish(comboDto);
        return R.success("Add combo successfully");
    }

    @GetMapping("/page")
    public R<Page<ComboDto>> pageCombo(int page, int pageSize, String name) {
        log.info("Combo list paging: page={}, pageSize={}", page, pageSize);
        Page<Combo> pageInfo = new Page<>(page, pageSize);
        Page<ComboDto> comboDtoPage = new Page<>();

        LambdaQueryWrapper<Combo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Combo::getName, name);
        queryWrapper.orderByDesc(Combo::getUpdateTime);
        comboService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, comboDtoPage, "records");
        List<Combo> records = pageInfo.getRecords();
        List<ComboDto> list = records.stream().map((item) -> {
            ComboDto comboDto = new ComboDto();
            BeanUtils.copyProperties(item, comboDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                comboDto.setCategoryName(categoryName);
            }
            return comboDto;
        }).collect(Collectors.toList());

        comboDtoPage.setRecords(list);
        return R.success(comboDtoPage);
    }

    @GetMapping("/{id}")
    public R<ComboDto> getById(@PathVariable Long id) {
        log.info("Get combo by id: id={}", id);
        ComboDto comboDto = comboService.getByIdWithComboDish(id);
        return R.success(comboDto);
    }

    @PutMapping
    public R<String> updateCombo(@RequestBody ComboDto comboDto) {
        log.info("Update dish: {}", comboDto.getName());
        comboService.updateWithComboDish(comboDto);
        return R.success("Update combo successfully");
    }

    @GetMapping("/list")
    public R<List<Combo>> list(Combo combo) {
        log.info("Combo list: {}", combo);
        LambdaQueryWrapper<Combo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(combo.getCategoryId() != null, Combo::getCategoryId, combo.getCategoryId());
        queryWrapper.eq(combo.getStatus() != null, Combo::getStatus, combo.getStatus());
        queryWrapper.orderByDesc(Combo::getUpdateTime);
        List<Combo> list = comboService.list(queryWrapper);

        return R.success(list);
    }
}
