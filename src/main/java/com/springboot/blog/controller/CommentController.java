package com.springboot.blog.controller;

import com.springboot.blog.dto.CommentDTO;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{post-id}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable("post-id") Long postId, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{post-id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable("post-id") Long postId) {
        return new ResponseEntity<>(commentService.findAllCommentsByPostId(postId), HttpStatus.OK);
    }

    @GetMapping("/posts/{post-id}/comments/{comment-id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable("post-id") Long postId, @PathVariable("comment-id") Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    @PutMapping("/posts/{post-id}/comments/{comment-id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("post-id") Long postId, @PathVariable("comment-id") Long commentId, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.updateComment(postId, commentId, commentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{post-id}/comments/{comment-id}")
    public ResponseEntity<String> deleteComment(@PathVariable("post-id") Long postId, @PathVariable("comment-id") Long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
