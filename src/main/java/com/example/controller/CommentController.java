package com.example.controller;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Comment;
import com.example.model.User;
import com.example.repository.CommentRepository;
import com.example.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    @GetMapping("/voitures/{voitureId}/comments")
    public Page<Comment> getAllCommentsByVoitureId(@PathVariable(value = "voitureId") Long voitureId,
            Pageable pageable) {
        return commentRepository.findByVoitureId(voitureId, pageable);
    }

    @PostMapping("/voitures/{voitureId}/comments")
    public Comment createComment(@CurrentSecurityContext(expression = "authentication") User user,
            @PathVariable(value = "voitureId") Long voitureId, @Validated @RequestBody Comment comment) {
        return voitureRepository.findById(voitureId).map(voiture -> {
            comment.setVoiture(voiture);
            comment.setUser(user);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("voitureId " + voitureId + " not found"));
    }

    @PutMapping("/voitures/{voitureId}/comments/{commentId}")
    public Comment updateComment(@PathVariable(value = "voitureId") Long voitureId,
            @PathVariable(value = "commentId") Long commentId, @Validated @RequestBody Comment commentRequest) {
        if (!voitureRepository.existsById(voitureId)) {
            throw new ResourceNotFoundException("voitureId " + voitureId + " not found");
        }

        return commentRepository.findById(commentId).map(comment -> {
            comment.setText(commentRequest.getText());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + "not found"));
    }

    @DeleteMapping("/voitures/{voitureId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "voitureId") Long voitureId,
            @PathVariable(value = "commentId") Long commentId) {
        return commentRepository.findByIdandVoitureId(commentId, voitureId).map(comment -> {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(
                "Comment not found with id " + commentId + " and voitureId " + voitureId));
    }
}