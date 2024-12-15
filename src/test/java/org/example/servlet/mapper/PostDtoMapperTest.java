package org.example.servlet.mapper;

import org.example.model.Post;
import org.example.servlet.dto.IncomingDtoPost;
import org.example.servlet.dto.OutGoingDtoPost;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PostDtoMapperTest {

    @Test
    void incomingDtoToPost() {
        IncomingDtoPost incomingDto = new IncomingDtoPost();
        incomingDto.setTitle("Test Post Title");
        incomingDto.setContent("Test Post Content");

        Post post = PostDtoMapper.INSTANCE.incomingDtoToPost(incomingDto);

        assertNotNull(post);
        assertEquals("Test Post Title", post.getTitle());
        assertEquals("Test Post Content", post.getContent());
        assertNull(post.getId()); // ID должно быть null, если не задано в IncomingDtoPost
    }

    @Test
    void postToOutGoingDto() {
        Post post = new Post(UUID.randomUUID(), "Test Post Title", "Test Post Content", UUID.randomUUID());

        OutGoingDtoPost outGoingDto = PostDtoMapper.INSTANCE.postToOutGoingDto(post);

        assertNotNull(outGoingDto);
        assertEquals(post.getId(), outGoingDto.getId());
        assertEquals(post.getTitle(), outGoingDto.getTitle());
        assertEquals(post.getContent(), outGoingDto.getContent());
    }

    @Test
    void incomingDtoToPost_nullInput() {
        Post post = PostDtoMapper.INSTANCE.incomingDtoToPost(null);
        assertNull(post);
    }

    @Test
    void postToOutGoingDto_nullInput() {
        OutGoingDtoPost outGoingDto = PostDtoMapper.INSTANCE.postToOutGoingDto(null);
        assertNull(outGoingDto);
    }
}
