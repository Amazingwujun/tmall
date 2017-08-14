package com.tmall.service;

import com.tmall.entity.po.Category;

import java.util.List;

public interface ICategoryService {

    /**
     *
     * @param category
     * @return
     */
    boolean addCategory(Category category);

    /**
     *
     * @param category
     * @return
     */
    boolean updateCategory(Category category);

    /**
     *
     *
     * @param categoryId
     * @return
     */
    List<Category> getSubCategory(Integer categoryId);
}
