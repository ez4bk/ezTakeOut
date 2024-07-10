package com.ez4bk.eztakeout.dto;

import com.ez4bk.eztakeout.entity.Combo;
import com.ez4bk.eztakeout.entity.ComboDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComboDto extends Combo {

    private List<ComboDish> comboDishes;

    private String categoryName;

}
