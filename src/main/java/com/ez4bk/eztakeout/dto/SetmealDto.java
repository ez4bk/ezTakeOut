package com.ez4bk.eztakeout.dto;

import com.ez4bk.eztakeout.entity.Setmeal;
import com.ez4bk.eztakeout.entity.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
