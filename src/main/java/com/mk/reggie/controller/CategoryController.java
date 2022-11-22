package com.mk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.reggie.common.R;
import com.mk.reggie.entity.Category;
import com.mk.reggie.entity.DishFlavor;
import com.mk.reggie.service.ICategoryService;
import com.mk.reggie.service.IDishFlavorService;
import com.mk.reggie.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IDishFlavorService dishFlavorService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("新建菜品分类...category：{}",category);
        if(category!=null){
           categoryService.save(category);
        }
        return R.success("添加成功");
    }
    @GetMapping("/page")
    public R<Page> page(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10")int pageSize){

        log.info("page={},pageSize={}",page,pageSize);
        Page pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.orderByDesc(Category::getCreateTime);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> deleteCategory(Long ids){
            log.info("删除id：{}",ids);
            categoryService.remove(ids);
            return R.success("删除成功");
        }
        @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        log.info("修改分类信息：{}",category.toString());
        categoryService.updateById(category);
        return R.success("更新成功");
        }

    /**
     * 菜品分类下拉选择框
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByDesc(Category::getType,Category::getCreateTime);
        List list=categoryService.list(queryWrapper);
        return R.success(list);

    }

}
