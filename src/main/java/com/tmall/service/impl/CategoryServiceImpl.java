package com.tmall.service.impl;

import com.tmall.dao.DBDao.CategoryDao;
import com.tmall.entity.po.Category;
import com.tmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryDao categoryDao;

    /**
     * 增加品类
     *
     * @param category
     * @return
     */
    @Override
    @Transactional
    public boolean addCategory(Category category) {
        Assert.notNull(category, "新增品类对象不能为空");

        Integer parentId = category.getParentId();

        //判断父品类是否存在
        if (parentId != 0) {
            Category parent = categoryDao.selectByPrimaryKey(parentId);
            if (parent == null) {
                return false;
            }
        }

        int result = categoryDao.insertSelective(category);

        return result > 0;
    }

    /**
     * 更新品类
     *
     * @param category
     * @return
     */
    @Override
    public boolean updateCategory(Category category) {
        Assert.notNull(category, "更新品类对象不能为空");

        int result = categoryDao.updateByPrimaryKeySelective(category);
        return result > 0;
    }

    /**
     * 获取当前品类的子品类
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Category> getSubCategory(Integer categoryId) {
        Assert.notNull(categoryId,"品类ID不能为空");

        List<Category> categoryList = categoryDao.getSubCategoryById(categoryId);

        return categoryList;
    }
}
