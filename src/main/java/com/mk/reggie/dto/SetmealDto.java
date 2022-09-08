package com.mk.reggie.dto;


import com.mk.reggie.entity.Setmeal;
import com.mk.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
