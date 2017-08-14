package com.tmall.controller.manage;

import com.tmall.common.validatorOrder.category.Add;
import com.tmall.common.validatorOrder.category.Update;
import com.tmall.entity.po.Category;
import com.tmall.entity.vo.JSONObject;
import com.tmall.service.ICategoryService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 新增品类
     *
     * @param category 品类对象
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("add")
    public JSONObject add(@Validated(Add.class) Category category) {
        if (category.getParentId() == null) {
            category.setParentId(0);
        }

        boolean result = categoryService.addCategory(category);
        return result ? JSONObject.successWithMessage("新增品类成功") : JSONObject.error("新增品类失败", 1);
    }

    /**
     * 新增品类
     *
     * @param category 品类对象
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("update")
    public JSONObject update(@Validated(Update.class) Category category) {
        boolean result = categoryService.updateCategory(category);

        return result ? JSONObject.successWithMessage("更新品类成功") : JSONObject.error("更新品类失败", 1);
    }

    /**
     * 获得当前品类的子品类
     *
     * @param categoryId 品类ID
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("{categoryId}/getSubCategory")
    public JSONObject getSubCategory(@PathVariable("categoryId") Integer categoryId) {
        List<Category> categoryList = categoryService.getSubCategory(categoryId);

        return JSONObject.success(null, categoryList);
    }

    /**
     * 获得当前品类所有的子品类
     *
     * @param categoryId 品类ID
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("{categoryId}/getAllCategory")
    public JSONObject getAllCategory(@PathVariable("categoryId") Integer categoryId) {
        List<Category> resultList = new ArrayList<>();

        List<Category> result = categoryService.getSubCategory(categoryId);
        recursive(result,resultList);

        return JSONObject.success(null, resultList);
    }

    /**
     * 递归查询子品类
     *
     * @param list 需要进行查询的结果集
     * @param resultList 递归结果集
     */
    private void recursive(List<Category> list,List<Category> resultList) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        resultList.addAll(list);

        for (Category category : list) {
            Integer id = category.getId();
            List<Category> categoryList = categoryService.getSubCategory(id);
            recursive(categoryList,resultList);
        }
    }



}
