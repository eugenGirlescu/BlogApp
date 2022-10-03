package com.springboot.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    public List<PostDTO> content;
    public int pageNo;
    public int pageSize;
    public long totalElements;
    public int totalPages;
    public boolean last;
}
