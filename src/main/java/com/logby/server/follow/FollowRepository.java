package com.logby.server.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowingId(Long followingId);  // 나를 팔로우하는 수 (팔로워)

    long countByFollowerId(Long followerId);     // 내가 팔로우하는 수 (팔로잉)

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    void deleteByFollowerAndFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}
