package com.springboot.blog.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String description;
    private String content;
    private Set<CommentDTO> comments;
}
