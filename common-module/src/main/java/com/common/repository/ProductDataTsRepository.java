package com.common.repository;

import com.common.entity.ProductDataTs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductDataTsRepository extends JpaRepository<ProductDataTs, Long> {
    List<ProductDataTs> findAllByTakenInBetweenAndProductId(Date start, Date end, Long productId);

    @Query("SELECT p FROM ProductDataTs p WHERE p.takenIn > ?1 AND p.takenIn < ?2 AND p.productId IN ?3 ORDER BY p.productId, p.takenIn")
    List<ProductDataTs> findByProductIdsBetweenDate(Date start, Date end, List<Long> ids);

    @Query("SELECT avg(quantity)" +
            "FROM ProductDataTs " +
            "WHERE takenIn > ?1 AND takenIn < ?2 AND productId IN ?3 ")
    Double findAvgQuantityByProductIdsBetweenDate(Date start, Date end, List<Long> ids);

    @Query("SELECT min(takenIn) FROM ProductDataTs")
    ProductDataTs minDate();

    @Query("SELECT max(takenIn) FROM ProductDataTs")
    ProductDataTs maxDate();
}
