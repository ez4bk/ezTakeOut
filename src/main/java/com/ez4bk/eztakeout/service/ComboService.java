package com.ez4bk.eztakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ez4bk.eztakeout.dto.ComboDto;
import com.ez4bk.eztakeout.entity.Combo;

import java.util.List;

public interface ComboService extends IService<Combo> {

    void saveWithDish(ComboDto comboDto);

    void updateWithComboDish(ComboDto comboDto);

    ComboDto getByIdWithComboDish(Long id);

    void removeWithComboDish(List<Long> ids);
}
