package com.ez4bk.eztakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ez4bk.eztakeout.common.CustomException;
import com.ez4bk.eztakeout.dto.ComboDto;
import com.ez4bk.eztakeout.entity.Combo;
import com.ez4bk.eztakeout.entity.ComboDish;
import com.ez4bk.eztakeout.mapper.ComboMapper;
import com.ez4bk.eztakeout.service.ComboDishService;
import com.ez4bk.eztakeout.service.ComboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComboServiceImpl extends ServiceImpl<ComboMapper, Combo> implements ComboService {
    @Autowired
    private ComboDishService comboDishService;

    @Override
    @Transactional
    public void saveWithDish(ComboDto comboDto) {
        this.save(comboDto);
        Long comboId = comboDto.getId();
        List<ComboDish> dishes = comboDto.getComboDishes();
        dishes.forEach(dish -> {
            dish.setComboId(comboId);
        });
        comboDishService.saveBatch(dishes);
    }

    @Override
    @Transactional
    public void updateWithComboDish(ComboDto comboDto) {
        this.updateById(comboDto);

        LambdaQueryWrapper<ComboDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboDish::getComboId, comboDto.getId());
        comboDishService.remove(queryWrapper);

        List<ComboDish> comboDishes = comboDto.getComboDishes();
        comboDishes.forEach(dish -> {
            dish.setComboId(comboDto.getId());
        });
        comboDishService.saveBatch(comboDishes);
    }

    @Override
    public ComboDto getByIdWithComboDish(Long id) {
        Combo combo = this.getById(id);

        ComboDto comboDto = new ComboDto();
        BeanUtils.copyProperties(combo, comboDto);

        LambdaQueryWrapper<ComboDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboDish::getComboId, combo.getId());
        List<ComboDish> comboDishes = comboDishService.list(queryWrapper);
        comboDto.setComboDishes(comboDishes);

        return comboDto;
    }

    @Override
    @Transactional
    public void removeWithComboDish(List<Long> ids) {
        LambdaQueryWrapper<Combo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Combo::getId, ids);
        queryWrapper.eq(Combo::getStatus, 1);

        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("The dish is in use and cannot be deleted.");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<ComboDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ComboDish::getComboId, ids);
        comboDishService.remove(lambdaQueryWrapper);
    }
}
