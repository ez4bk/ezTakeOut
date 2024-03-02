package com.ez4bk.eztakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ez4bk.eztakeout.entity.Category;

public interface CategoryService extends IService<Category> {
    void removeByIdSafe(Long id);
}
