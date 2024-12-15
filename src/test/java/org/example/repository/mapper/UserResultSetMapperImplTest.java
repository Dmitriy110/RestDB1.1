package org.example.repository.mapper;

import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserResultSetMapperImplTest {

    @Test
    void map() throws SQLException {
        // Создаем мок ResultSet
        ResultSet resultSet = mock(ResultSet.class);

        // Устанавливаем предполагаемое поведение для мок-объекта
        when(resultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("johndoe@example.com");

        // Создаем экземпляр UserResultSetMapperImpl
        UserResultSetMapperImpl mapper = new UserResultSetMapperImpl();

        // Вызываем метод map()
        User user = mapper.map(resultSet);

        // Проверяем результаты
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe@example.com", user.getEmail());

        // Дополнительная проверка на то, что метод getString был вызван 3 раза
        verify(resultSet, times(3)).getString(anyString());
    }
}