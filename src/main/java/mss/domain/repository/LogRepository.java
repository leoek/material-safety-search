package mss.domain.repository;

import mss.domain.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LogRepository extends JpaRepository<Log, Long> {
    @Query(value = "select t from Log as t group by t.searchTerm order by count(t.searchTerm) desc")
    Page<Log> getAll(Pageable p);
}
