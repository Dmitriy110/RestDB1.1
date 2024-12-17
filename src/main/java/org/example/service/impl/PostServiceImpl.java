package org.example.service.impl;

import org.example.model.Post;
import org.example.repository.PostEntityRepository;
import org.example.service.Service;

import java.util.List;
import java.util.UUID;

public class PostServiceImpl implements Service<Post> {
    private PostEntityRepository postRepository;

    public PostServiceImpl(PostEntityRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public Post save(Post post) { return postRepository.save(post); }

    @Override
    public Post findById(UUID uuid) { return postRepository.findById(uuid); }

    @Override
    public List<Post> findAll() { return postRepository.findAll(); }

    @Override
    public boolean deleteById(UUID id) { return postRepository.deleteById(id); }
}
