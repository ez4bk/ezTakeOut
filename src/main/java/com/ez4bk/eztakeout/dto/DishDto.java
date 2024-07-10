package com.ez4bk.eztakeout.dto;

import com.ez4bk.eztakeout.entity.Dish;
import com.ez4bk.eztakeout.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
