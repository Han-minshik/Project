package com.project.mapper;

import com.project.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryMapper {
    Integer findCategoryByNameAndParent(@Param("name") String name, @Param("parent") Integer parent);

    void insertCategory(CategoryDTO categoryDTO);
}
