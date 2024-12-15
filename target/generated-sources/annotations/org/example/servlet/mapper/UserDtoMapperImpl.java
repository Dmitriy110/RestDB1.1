package org.example.servlet.mapper;

import javax.annotation.processing.Generated;
import org.example.model.User;
import org.example.servlet.dto.IncomingDtoUser;
import org.example.servlet.dto.OutGoingDtoUser;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T17:36:30+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public User incomingDtoToUser(IncomingDtoUser incomingDtoUser) {
        if ( incomingDtoUser == null ) {
            return null;
        }

        User user = new User();

        user.setName( incomingDtoUser.getName() );
        user.setEmail( incomingDtoUser.getEmail() );

        return user;
    }

    @Override
    public OutGoingDtoUser userToOutGoingDto(User user) {
        if ( user == null ) {
            return null;
        }

        OutGoingDtoUser outGoingDtoUser = new OutGoingDtoUser();

        outGoingDtoUser.setId( user.getId() );
        outGoingDtoUser.setName( user.getName() );
        outGoingDtoUser.setEmail( user.getEmail() );

        return outGoingDtoUser;
    }
}
