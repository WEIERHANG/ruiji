package com.mk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.reggie.common.R;
import com.mk.reggie.dto.SetmealDto;
import com.mk.reggie.entity.*;
import com.mk.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/setmeal")
@Slf4j
@RestController
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ISetmealDishService setmealDishService;


    @GetMapping("/page")
    public R<Page> page(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10")int pageSize,String name){
        log.info("分页信息--page:{},pageSize:{}",page,pageSize,name);
        //分页构造器
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name);
        //排序
        queryWrapper.orderByDesc(Setmeal::getCreateTime);
        //执行分页查询
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝--setmealDtoPage拷贝pageInfo的信息，不拷贝records
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records= pageInfo.getRecords();
        //item代表Dish对象
        List<SetmealDto> list= records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId= item.getCategoryId();//分类id
            //根据id查分类
            Category category=categoryService.getById(categoryId);
            if(categoryId!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("{}",setmealDto);
        setmealService.saveMealAndDish(setmealDto);
        return R.success("新增成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> editSetmeal(@PathVariable Long id){
        SetmealDto setmealDto=setmealService.editSetmealDish(id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> updateSetealDish(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetealDish(setmealDto);
        return R.success("更新成功");
    }

    @DeleteMapping
    public R<String> deleteSetealDish(@RequestParam List<Long> ids){
        setmealService.deleteSetealDish(ids);
        return R.success("删除成功");
    }

    @PostMapping("status/{number}")
    public R<String> updateStatus(@PathVariable int number, Long[] ids){
//        List<Long> idslist = Arrays.asList(ids);
        for(Long str:ids){
            Setmeal setmeal=setmealService.getById(str);
            if(number==0){
                setmeal.setStatus(0);
            }else {
                setmeal.setStatus(1);
            }
            setmealService.updateById(setmeal);
        }
        return R.success("状态更新成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list( Setmeal setmeal){
        //查找启用状态下的套餐与菜品对应关系
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
//        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.orderByDesc(Setmeal::getCreateTime);
        List<Setmeal> setmeals=setmealService.list(queryWrapper);
//只展示套餐信息，-----下面对应的菜品不需要展示
//        List<SetmealDto> setmealDtos=setmeals.stream().map((item)->{
//            //拷贝套餐信息
//            SetmealDto setmealDto=new SetmealDto();
//            BeanUtils.copyProperties(setmeals,setmealDto);
//            //构造查询套餐下的菜品信息
//           LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper=new LambdaQueryWrapper<>();
//           setmealDishLambdaQueryWrapper.eq(item.getStatus()!=0,SetmealDish::getSetmealId,item.getCategoryId());
//           List<SetmealDish> setmealDishes=setmealDishService.list(setmealDishLambdaQueryWrapper);
//           setmealDto.setSetmealDishes(setmealDishes);
//            log.info("{}",setmealDto);
//            return setmealDto;
//        }).collect(Collectors.toList());
        return R.success(setmeals);
    }

}
