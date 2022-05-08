package com.common.repository;

import com.common.entity.ProductData;
import com.common.utils.SqlDonutStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query(value = "SELECT s FROM ProductData s")
//    @QueryHints(@QueryHint(name = AvailableSettings.JPA_LOCK_TIMEOUT, value = "-2"/*SKIP_LOCKED constant*/))
//    List<ProductData> findAndLock(State state, Pageable pageable);
    List<ProductData> findAllByCategoryUrlContaining(String categoryUrl);

    @Query("SELECT distinct p.categoryUrl from ProductData p where p.categoryUrl like %:categoryUrl%")
    List<String> findDistinctByCategoryUrlContaining(@Param("categoryUrl") String categoryUrl);

    @Query("SELECT distinct new com.common.utils.SqlDonutStatistic(p.brandName, count(p.brandName))" +
            "from ProductData p where p.categoryUrl like %:categoryUrl% group by p.brandName")
    List<SqlDonutStatistic> findDistinctCountByBrandNameContaining(@Param("categoryUrl") String categoryUrl);
}
