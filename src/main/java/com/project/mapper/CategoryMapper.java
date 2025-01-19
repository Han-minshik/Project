package com.project.mapper;

import com.project.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryMapper {
    void insertCategory(CategoryDTO category);
    CategoryDTO findCategoryByNameAndParent(@Param("name") String name, @Param("parent") Integer parent);
    CategoryDTO findByName(String name);
}


