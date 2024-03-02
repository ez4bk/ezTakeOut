package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.entity.Setmeal;
import com.ez4bk.eztakeout.mapper.SetmealMapper;
import com.ez4bk.eztakeout.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
