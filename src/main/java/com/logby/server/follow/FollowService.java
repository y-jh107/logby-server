package com.logby.server.follow;

import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.user.User;
import com.logby.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(Long followerId, Long targetUserId) {
        if (followerId.equals(targetUserId)) {
            throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF);
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, targetUserId)) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWING);
        }

        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(targetUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        followRepository.save(Follow.builder()
            .follower(follower)
            .following(following)
            .build());
    }

    @Transactional
    public void unfollow(Long followerId, Long targetUserId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, targetUserId)) {
            throw new BusinessException(ErrorCode.NOT_FOLLOWING);
        }
        followRepository.deleteByFollowerAndFollowing(followerId, targetUserId);
    }
}
