package com.logby.server.log;

import com.logby.server.comment.CommentRepository;
import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.content.ContentRepository;
import com.logby.server.content.dto.ContentResponse;
import com.logby.server.follow.FollowRepository;
import com.logby.server.like.LikeRepository;
import com.logby.server.log.dto.LogCreateRequest;
import com.logby.server.log.dto.LogResponse;
import com.logby.server.user.User;
import com.logby.server.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;

    @Transactional
    public LogResponse create(Long userId, LogCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Log log = Log.builder()
            .user(user)
            .title(request.title())
            .body(request.body())
            .visibility(request.visibility())
            .build();

        return LogResponse.from(logRepository.save(log));
    }

    public List<LogResponse> getMyLogs(Long userId) {
        return logRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(LogResponse::from)
            .toList();
    }

    // 단건 상세: 접근 권한 확인 후 counts + likedByMe 포함
    public LogResponse getLog(Long logId, Long userId) {
        Log log = logRepository.findById(logId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));

        boolean isOwner = log.getUser().getId().equals(userId);
        if (!isOwner) {
            Visibility vis = log.getVisibility();
            if (vis == Visibility.PRIVATE) {
                throw new BusinessException(ErrorCode.LOG_ACCESS_DENIED);
            }
            if (vis == Visibility.FOLLOWERS_ONLY) {
                boolean follows = followRepository.existsByFollowerIdAndFollowingId(userId, log.getUser().getId());
                if (!follows) throw new BusinessException(ErrorCode.LOG_ACCESS_DENIED);
            }
        }

        List<ContentResponse> contents = contentRepository.findByLogIdOrderBySortOrder(logId)
            .stream().map(ContentResponse::from).toList();

        long likeCount = likeRepository.countByLogId(logId);
        long commentCount = commentRepository.countByLogId(logId);
        boolean likedByMe = likeRepository.existsByLogIdAndUserId(logId, userId);

        return LogResponse.of(log, contents, likeCount, commentCount, likedByMe);
    }

    @Transactional
    public LogResponse update(Long logId, Long userId, LogCreateRequest request) {
        Log log = logRepository.findById(logId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));

        if (!log.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.LOG_ACCESS_DENIED);
        }

        log.update(request.title(), request.body(), request.visibility());
        return LogResponse.from(log);
    }

    public Page<LogResponse> getFeed(Long userId, Pageable pageable) {
        List<Visibility> feedVisibilities = List.of(Visibility.PUBLIC, Visibility.FOLLOWERS_ONLY);
        return logRepository.findFeedLogs(userId, feedVisibilities, pageable)
            .map(log -> LogResponse.feed(
                log,
                likeRepository.countByLogId(log.getId()),
                commentRepository.countByLogId(log.getId())
            ));
    }

    public Page<LogResponse> getUserPublicLogs(Long targetUserId, Pageable pageable) {
        return logRepository.findByUserIdAndVisibility(targetUserId, Visibility.PUBLIC, pageable)
            .map(log -> LogResponse.feed(
                log,
                likeRepository.countByLogId(log.getId()),
                commentRepository.countByLogId(log.getId())
            ));
    }

    @Transactional
    public void delete(Long logId, Long userId) {
        Log log = logRepository.findById(logId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));

        if (!log.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.LOG_ACCESS_DENIED);
        }

        logRepository.delete(log);
    }
}
