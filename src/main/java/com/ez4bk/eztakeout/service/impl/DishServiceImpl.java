package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.entity.Dish;
import com.ez4bk.eztakeout.mapper.DishMapper;
import com.ez4bk.eztakeout.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
