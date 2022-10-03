package com.springboot.blog.service.impl;

import com.springboot.blog.dto.PostDTO;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = new Post();

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        Post savedPost = postRepository.save(post);

        PostDTO responseDto = new PostDTO();

        responseDto.setId(savedPost.getId());
        responseDto.setTitle(savedPost.getTitle());
        responseDto.setDescription(savedPost.getDescription());
        responseDto.setContent(savedPost.getContent());

        return responseDto;

    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDTO> content = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(Long id) {

        Post post = getPost(id);

        return mapToDto(post);

    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {
        Post post = getPost(id);

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    private Post getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return post;
    }

    @Override
    public void deletePostById(Long id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    private PostDTO mapToDto(Post post) {
        PostDTO postDto = modelMapper.map(post, PostDTO.class);

        return postDto;
    }

    private Post mapToEntity(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);

        return post;
    }
}
