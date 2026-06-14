package com.logby.server.comment;

import com.logby.server.comment.dto.CommentRequest;
import com.logby.server.comment.dto.CommentResponse;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(Long logId, Long userId, CommentRequest request) {
        Log log = logRepository.findById(logId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
            .log(log)
            .user(user)
            .body(request.body())
            .build();

        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional
    public void delete(Long logId, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getLog().getId().equals(logId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (!comment.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }
}
