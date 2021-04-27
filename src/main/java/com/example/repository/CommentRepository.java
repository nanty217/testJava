package com.example.repository;

import java.util.Optional;

import com.example.model.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByVoitureId(Long voitureId, Pageable pageable);
    Page<Comment> findByUserId(Long userId,Pageable pageable);
    Optional<Comment> findByIdandVoitureId(Long id, Long voitureId);
}