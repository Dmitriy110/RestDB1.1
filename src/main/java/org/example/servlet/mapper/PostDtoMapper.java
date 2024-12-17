package org.example.servlet.mapper;


import org.example.model.Post;
import org.example.servlet.dto.IncomingDtoPost;
import org.example.servlet.dto.OutGoingDtoPost;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostDtoMapper {
    PostDtoMapper INSTANCE = Mappers.getMapper(PostDtoMapper.class);

    Post incomingDtoToPost(IncomingDtoPost incomingDtoPost);
    OutGoingDtoPost postToOutGoingDto(Post post);
}
