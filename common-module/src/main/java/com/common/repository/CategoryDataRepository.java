package com.common.repository;

import com.common.entity.CategoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDataRepository extends JpaRepository<CategoryData, Long> {
    List<CategoryData> findAllByParentId(Long parentId);

    CategoryData findTopByPageUrl(String pageUrl);
}
