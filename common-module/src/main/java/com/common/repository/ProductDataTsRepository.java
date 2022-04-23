package com.common.repository;

import com.common.entity.ProductDataTs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDataTsRepository extends JpaRepository<ProductDataTs, Long> {
}
