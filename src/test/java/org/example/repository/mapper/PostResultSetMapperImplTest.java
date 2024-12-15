package org.example.repository.mapper;

import org.example.model.Post;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostResultSetMapperImplTest {

    @Test
    void map() throws SQLException {
        // Создаем мок ResultSet
        ResultSet resultSet = mock(ResultSet.class);

        // Устанавливаем ожидаемое поведение для мок-объекта
        String postId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String title = "Test Title";
        String content = "Test Content";

        when(resultSet.getString("id")).thenReturn(postId);
        when(resultSet.getString("title")).thenReturn(title);
        when(resultSet.getString("content")).thenReturn(content);
        when(resultSet.getString("user_id")).thenReturn(userId);

        // Создаем экземпляр PostResultSetMapperImpl
        PostResultSetMapperImpl mapper = new PostResultSetMapperImpl();

        // Вызываем метод map()
        Post post = mapper.map(resultSet);

        // Проверяем результаты
        assertNotNull(post);
        assertEquals(UUID.fromString(postId), post.getId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
        assertEquals(UUID.fromString(userId), post.getUserId());

        // Проверяем, что методы getString были вызваны
        verify(resultSet, times(4)).getString(anyString());
    }

    @Test
    void map_withNullResultSet() throws SQLException {
        PostResultSetMapperImpl mapper = new PostResultSetMapperImpl();

        // Проверяем, что метод возвращает null
        Post post = mapper.map(null);
        assertNull(post);
    }
}
