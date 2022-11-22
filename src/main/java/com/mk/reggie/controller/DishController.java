package com.mk.reggie.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.reggie.common.R;
import com.mk.reggie.dto.DishDto;
import com.mk.reggie.entity.Category;
import com.mk.reggie.entity.Dish;
import com.mk.reggie.entity.DishFlavor;
import com.mk.reggie.service.ICategoryService;
import com.mk.reggie.service.IDishFlavorService;
import com.mk.reggie.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private IDishService dishService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;



    @GetMapping("/page")
    public R<Page> page(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10")int pageSize,String name){
        log.info("分页信息--page:{},pageSize:{}",page,pageSize,name);
        //分页构造器
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name),Dish::getName,name);
        //排序
        queryWrapper.orderByDesc(Dish::getCreateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝--dishDtoPage拷贝pageInfo的信息，不拷贝records
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records= pageInfo.getRecords();
        //item代表Dish对象
        List<DishDto> list= records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId= item.getCategoryId();//分类id
            //根据id查分类
            Category category=categoryService.getById(categoryId);
            if(categoryId!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        //清理某个分类下面的菜品缓存数据
        String key="dish_"+dishDto.getCategoryId()+"*";
        redisTemplate.delete(key);
        return R.success("新增成功");
    }

    /**
     * 根据id查询菜品信息和菜品口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> editDish(@PathVariable Long id){
        //查询菜品信息和对应口味信息
        DishDto dishAndFlavors=dishService.selectFlavors(id);
        return R.success(dishAndFlavors);
    }

    @PutMapping
    public R<String> updateDishAndFlavors(@RequestBody DishDto dishDto){
        dishService.updateDishAndFlavors(dishDto);

        //清理所有菜品的缓存数据
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        //清理某个分类下面的菜品缓存数据
        String key="dish_"+dishDto.getCategoryId()+"*";
        redisTemplate.delete(key);

        return R.success("更新成功");
    }

    @DeleteMapping
    public R<String> deleteDishFlavors(Long[] ids){
        List<Long> list=Arrays.asList(ids);
        //批量删除
        dishService.removeByIds(list);
        //删除口味信息
               for(Long str:ids){
                   LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
                   queryWrapper.eq(DishFlavor::getDishId,str);
                   dishFlavorService.remove(queryWrapper);
               }
        //用sql删除口味信息
//        dishFlavorService.removeFlavors(ids);
          return R.success("删除成功");
    }

    /**
     * 停售,起售
     * @param number
     * @param ids
     * @return
     */
    @PostMapping("/status/{number}")
    public R<String> updateStatus(@PathVariable int number,Long[] ids){
        for(Long str:ids){
            Dish dish=dishService.getById(str);
            if(number==0){
                dish.setStatus(0);
            }else {
                dish.setStatus(1);
            }
            dishService.updateById(dish);
        }
        return R.success("状态已更新");
    }

//    /**
//     * 根据条件查询菜品数据
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //构造
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        //过滤
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        //只显示起售状态的信息
//        queryWrapper.eq(Dish::getStatus,1);
//        //排序条件
//        queryWrapper.orderByAsc(Dish::getCreateTime).orderByDesc(Dish::getUpdateTime);
//        List list=dishService.list(queryWrapper);
//        return R.success(list);
//    }

    /**
     * 优化根据条件查询菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList=null;
       //先从redis查找dishlist数据，如果没有就从数据库中查找存入redis
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(CollectionUtils.isNotEmpty(dishDtoList)){
            return  R.success(dishDtoList);
        }else {
            //构造
            LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
            //过滤
            queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
            //只显示起售状态的信息
            queryWrapper.eq(Dish::getStatus,1);
            //排序条件
            queryWrapper.orderByAsc(Dish::getCreateTime).orderByDesc(Dish::getUpdateTime);
            List<Dish> list= dishService.list(queryWrapper);

            dishDtoList=list.stream().map((item)->{
                DishDto dishDto=new DishDto();
                BeanUtils.copyProperties(item,dishDto);
                Category category=categoryService.getById(dish.getCategoryId());
                if(category!=null){
                    dishDto.setCategoryName(category.getName());
                }
                //当前菜品id
                Long dishId= item.getId();
                LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper=new LambdaQueryWrapper<>();
                dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
                List<DishFlavor> dishFlavorList=dishFlavorService.list(dishFlavorLambdaQueryWrapper);
                dishDto.setFlavors(dishFlavorList);
                return dishDto;
            }).collect(Collectors.toList());
        }
        //缓存redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
}
