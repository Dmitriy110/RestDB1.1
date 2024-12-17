package org.example.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Post;
import org.example.repository.PostEntityRepository;
import org.example.repository.mapper.PostResultSetMapperImpl;
import org.example.repository.mapper.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostEntityRepositoryImpl implements PostEntityRepository {
    private final ResultSetMapper<Post> resultSetMapper = new PostResultSetMapperImpl();
    private final HikariDataSource dataSource;

    public PostEntityRepositoryImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Post findById(UUID id) {
        if (id == null) { throw new IllegalArgumentException("ID cannot be null"); }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT id, title, content, user_id FROM posts WHERE id = ?")) {
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetMapper.map(resultSet);
                } else {
                    return null; // Пользователь не найден
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска сообщения по идентификатору: " + id, e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("DELETE FROM posts WHERE id = ?")) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0; // Возвращает true, если строка была удалена
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления сообщения по идентификатору: " + id, e);
        }
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement(); // Используем Statement, так как нет параметров
             ResultSet resultSet = statement.executeQuery("SELECT id, title, content, user_id FROM posts")) {
            while (resultSet.next()) {
                posts.add(resultSetMapper.map(resultSet));
            }
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при загрузке всех posts", e);
        }
    }

    @Override
    public Post save(Post post) {

        String sql = "INSERT INTO posts (id, title, content, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, post.getId());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setObject(4, post.getUserId());
            preparedStatement.executeUpdate();
            return post;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения поста: " + post, e);
        }
    }
}
