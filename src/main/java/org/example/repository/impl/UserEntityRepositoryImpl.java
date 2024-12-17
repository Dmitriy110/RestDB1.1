package org.example.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Post;
import org.example.model.User;
import org.example.repository.UserEntityRepository;
import org.example.repository.mapper.ResultSetMapper;
import org.example.repository.mapper.UserResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserEntityRepositoryImpl implements UserEntityRepository {
    private final ResultSetMapper<User> resultSetMapper = new UserResultSetMapperImpl();
    private final HikariDataSource dataSource;

    public UserEntityRepositoryImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public User findById(UUID id) {
        if (id == null) { throw new IllegalArgumentException("ID cannot be null"); }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT id, name, email FROM users WHERE id = ?")) {
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = resultSetMapper.map(resultSet);
                    //Загрузка связанных постов
                    try {
                        user.setPost(loadPostsForUser(connection, user.getId()));
                    } catch (SQLException e) {
                        System.err.println("Предупреждение: Ошибка загрузки сообщений для пользователя " + e.getMessage());
                    }
                    return user;
                } else {
                    return null; // Пользователь не найден
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка в findById: " + e.getMessage());
        }
        return null;
    }

    private List<Post> loadPostsForUser(Connection connection, UUID userId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, content, user_id FROM posts WHERE user_id = ?")) {
            statement.setObject(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID id = resultSet.getObject("id", UUID.class);
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    UUID user_id = resultSet.getObject("user_id", UUID.class); // Извлекаем user_id
                    posts.add(new Post(id, title, content, user_id));
                }
            } catch (SQLException e) {
                posts = new ArrayList<>();
                System.out.println("Нет связанных постов для этого пользователя");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка в loadPostsForUser: " + e.getMessage());
        }
        return posts;
    }

    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0; // Возвращает true, если строка была удалена
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя по ID: " + id, e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email FROM users";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(resultSetMapper.map(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при извлечении всех пользователей ", e);
        }
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения user: " + user, e);
        }
    }
}