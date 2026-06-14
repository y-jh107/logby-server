package com.logby.server.log;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Log> findByUserIdAndVisibility(Long userId, Visibility visibility, Pageable pageable);

    long countByUserIdAndVisibility(Long userId, Visibility visibility);

    @Query(
        value = """
            SELECT l FROM Log l
            WHERE l.user.id IN (
                SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId
            )
            AND l.visibility IN :visibilities
            """,
        countQuery = """
            SELECT COUNT(l) FROM Log l
            WHERE l.user.id IN (
                SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId
            )
            AND l.visibility IN :visibilities
            """
    )
    Page<Log> findFeedLogs(
        @Param("userId") Long userId,
        @Param("visibilities") List<Visibility> visibilities,
        Pageable pageable
    );
}
