package org.example.servlet.mapper;


import org.example.model.User;
import org.example.servlet.dto.IncomingDtoUser;
import org.example.servlet.dto.OutGoingDtoUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    User incomingDtoToUser(IncomingDtoUser incomingDtoUser);
    OutGoingDtoUser userToOutGoingDto(User user);

}