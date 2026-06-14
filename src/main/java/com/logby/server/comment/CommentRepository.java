package com.logby.server.comment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByLogId(Long logId);

    List<Comment> findByLogIdOrderByCreatedAtAsc(Long logId);
}
