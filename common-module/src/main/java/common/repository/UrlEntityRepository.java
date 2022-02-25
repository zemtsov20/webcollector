package common.repository;

import common.entity.UrlEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlEntityRepository extends CrudRepository<UrlEntity, Long> {
}
