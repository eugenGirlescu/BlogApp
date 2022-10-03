package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long id, CommentDTO commentDTO);

    List<CommentDTO> findAllCommentsByPostId(Long id);

    CommentDTO getCommentById(Long postId, Long commentId);

    CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO);

    void deleteCommentById(Long postId, Long commentId);

}
