package com.logby.server.like;

import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.log.Log;
import com.logby.server.log.LogRepository;
import com.logby.server.user.User;
import com.logby.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(Long logId, Long userId) {
        if (likeRepository.existsByLogIdAndUserId(logId, userId)) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        Log log = logRepository.findById(logId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        likeRepository.save(Like.builder().log(log).user(user).build());
    }

    @Transactional
    public void unlike(Long logId, Long userId) {
        if (!likeRepository.existsByLogIdAndUserId(logId, userId)) {
            throw new BusinessException(ErrorCode.NOT_LIKED);
        }
        likeRepository.deleteByLogAndUser(logId, userId);
    }
}
