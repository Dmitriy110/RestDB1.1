package org.example.servlet.mapper;

import javax.annotation.processing.Generated;
import org.example.model.Post;
import org.example.servlet.dto.IncomingDtoPost;
import org.example.servlet.dto.OutGoingDtoPost;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T17:36:30+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
public class PostDtoMapperImpl implements PostDtoMapper {

    @Override
    public Post incomingDtoToPost(IncomingDtoPost incomingDtoPost) {
        if ( incomingDtoPost == null ) {
            return null;
        }

        Post post = new Post();

        post.setTitle( incomingDtoPost.getTitle() );
        post.setContent( incomingDtoPost.getContent() );

        return post;
    }

    @Override
    public OutGoingDtoPost postToOutGoingDto(Post post) {
        if ( post == null ) {
            return null;
        }

        OutGoingDtoPost outGoingDtoPost = new OutGoingDtoPost();

        outGoingDtoPost.setId( post.getId() );
        outGoingDtoPost.setTitle( post.getTitle() );
        outGoingDtoPost.setContent( post.getContent() );

        return outGoingDtoPost;
    }
}
