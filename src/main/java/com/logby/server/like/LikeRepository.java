package com.logby.server.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByLogIdAndUserId(Long logId, Long userId);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.log.id = :logId AND l.user.id = :userId")
    void deleteByLogAndUser(@Param("logId") Long logId, @Param("userId") Long userId);
}
