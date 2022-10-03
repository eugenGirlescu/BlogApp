package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDTO;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDTO createComment(Long id, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);

        Post post = getPost(id);

        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDTO> findAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllCommentsByPostId(postId);

        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        Post post = getPost(postId);

        Comment comment = getComment(commentId);

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) {
        Post post = getPost(postId);

        Comment comment = getComment(commentId);

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }


        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setPost(post);
        comment.setMessageBody(commentDTO.getMessageBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);

    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Post post = getPost(postId);
        Comment comment = getComment(commentId);

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        commentRepository.delete(comment);
    }

    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        return comment;
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        return comment;
    }

    private Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return post;
    }
}
