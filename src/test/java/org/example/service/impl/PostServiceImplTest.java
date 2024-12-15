package org.example.service.impl;


import org.example.model.Post;
import org.example.repository.PostEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostEntityRepository postRepository;

    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postService = new PostServiceImpl(postRepository);
    }

    @Test
    void save() {
        Post post = new Post(UUID.randomUUID(), "Test Post Title", "Test Post Content", UUID.randomUUID());
        when(postRepository.save(post)).thenReturn(post);

        Post savedPost = postService.save(post);
        assertEquals(post, savedPost);
        verify(postRepository).save(post);
    }

    @Test
    void findById_postFound() {
        UUID testUUID = UUID.randomUUID();
        Post mockPost = new Post(testUUID, "Test Post Title", "Test Post Content", UUID.randomUUID());
        when(postRepository.findById(testUUID)).thenReturn(mockPost);

        Post foundPost = postService.findById(testUUID);
        assertEquals(mockPost, foundPost);
        verify(postRepository).findById(testUUID);
    }

    @Test
    void findById_postNotFound() {
        UUID testUUID = UUID.randomUUID();
        when(postRepository.findById(testUUID)).thenReturn(null);

        Post foundPost = postService.findById(testUUID);
        assertNull(foundPost);
        verify(postRepository).findById(testUUID);
    }

    @Test
    void findAll() {
        List<Post> mockPosts = List.of(
                new Post(UUID.randomUUID(), "Post 1", "Content 1", UUID.randomUUID()),
                new Post(UUID.randomUUID(), "Post 2", "Content 2", UUID.randomUUID())
        );
        when(postRepository.findAll()).thenReturn(mockPosts);

        List<Post> allPosts = postService.findAll();
        assertEquals(mockPosts, allPosts);
        verify(postRepository).findAll();
    }

    @Test
    void createTable() {
        postService.createTable();
        verify(postRepository).createTable();
    }

    @Test
    void deleteById_postExists() {
        UUID testUUID = UUID.randomUUID();
        when(postRepository.deleteById(testUUID)).thenReturn(true);
        assertTrue(postService.deleteById(testUUID));
        verify(postRepository).deleteById(testUUID);
    }

    @Test
    void deleteById_postNotExists() {
        UUID testUUID = UUID.randomUUID();
        when(postRepository.deleteById(testUUID)).thenReturn(false);
        assertFalse(postService.deleteById(testUUID));
        verify(postRepository).deleteById(testUUID);
    }
}

