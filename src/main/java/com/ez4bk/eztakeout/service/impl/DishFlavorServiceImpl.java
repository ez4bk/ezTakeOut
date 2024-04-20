package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.entity.DishFlavor;
import com.ez4bk.eztakeout.mapper.DishFlavorMapper;
import com.ez4bk.eztakeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
