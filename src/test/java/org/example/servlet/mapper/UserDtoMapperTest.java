package org.example.servlet.mapper;

import org.example.model.User;
import org.example.servlet.dto.IncomingDtoUser;
import org.example.servlet.dto.OutGoingDtoUser;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoMapperTest {

    @Test
    void incomingDtoToUser() {
        IncomingDtoUser incomingDto = new IncomingDtoUser();
        incomingDto.setName("Test User");
        incomingDto.setEmail("test@example.com");

        User user = UserDtoMapper.INSTANCE.incomingDtoToUser(incomingDto);

        assertNotNull(user);
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertNull(user.getId()); // ID должно быть null, если не задано в IncomingDtoUser
    }

    @Test
    void userToOutGoingDto() {
        User user = new User(UUID.randomUUID(), "Test User", "test@example.com");

        OutGoingDtoUser outGoingDto = UserDtoMapper.INSTANCE.userToOutGoingDto(user);

        assertNotNull(outGoingDto);
        assertEquals(user.getId(), outGoingDto.getId());
        assertEquals(user.getName(), outGoingDto.getName());
        assertEquals(user.getEmail(), outGoingDto.getEmail());
    }

    @Test
    void incomingDtoToUser_nullInput() {
        User user = UserDtoMapper.INSTANCE.incomingDtoToUser(null);
        assertNull(user);
    }

    @Test
    void userToOutGoingDto_nullInput() {
        OutGoingDtoUser outGoingDto = UserDtoMapper.INSTANCE.userToOutGoingDto(null);
        assertNull(outGoingDto);
    }
}
