package org.example.repository.impl;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Post;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;


@Testcontainers
class PostEntityRepositoryImplTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    private static HikariDataSource dataSource;
    private PostEntityRepositoryImpl postRepository;

    @BeforeEach
    void setUpTable() throws SQLException {
        postgreSQLContainer.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.setMaximumPoolSize(10); // Настройка пула соединений - не забудьте изменить
        dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE posts (
                        id UUID PRIMARY KEY,
                        title VARCHAR(255),
                        content VARCHAR(255),
                        user_id UUID
                    )
                    """);
        }
        postRepository = new PostEntityRepositoryImpl(dataSource);
    }

    @AfterEach
    void tearDownTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS posts");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы posts: " + e.getMessage());
        }
    }

    @Test
    void findAll() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid1_1 = UUID.randomUUID();
        Post post1 = new Post(uuid1, "Post 1", "Test Post Content", uuid1_1);
        UUID uuid2 = UUID.randomUUID();
        UUID uuid2_1 = UUID.randomUUID();
        Post post2 = new Post(uuid2, "Post 2", "Test Post Content", uuid2_1);
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> posts = postRepository.findAll();
        assertEquals(2, posts.size());
        assertTrue(posts.contains(post1));
        assertTrue(posts.contains(post2));
    }

    @Test
    void save() throws SQLException {
        Post post = new Post(UUID.randomUUID(), "Test Post Title", "Test Post Content", UUID.randomUUID());
        Post savedpost = postRepository.save(post);
        assertNotNull(savedpost);
        assertNotNull(savedpost.getId());
        assertEquals("Test Post Title", savedpost.getTitle());
        assertEquals("Test Post Content", savedpost.getContent());

        // Проверка наличия записи в базе данных
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE id = ?")) {
            statement.setObject(1, savedpost.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next()); // проверяем наличие строки в БД
            }
        }
    }

    @Test
    void findById() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid1_1 = UUID.randomUUID();
        Post post = new Post(uuid1, "Test Post Title", "Test Post Content", uuid1_1);
        postRepository.save(post);

        Post foundPost = postRepository.findById(uuid1);
        assertNotNull(foundPost);
        assertEquals(post, foundPost);
    }

    @Test
    void findById_notFound()  {
        UUID testUUID = UUID.randomUUID();
        assertNull(postRepository.findById(testUUID));
    }

    @Test
    void testFindById_nullId() {
        assertThrows(IllegalArgumentException.class, () -> postRepository.findById(null));
    }


    @Test
    void deleteById() throws SQLException {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid1_1 = UUID.randomUUID();
        Post post = new Post(uuid1, "Test Post Title", "Test Post Content", uuid1_1);
        postRepository.save(post);

        assertTrue(postRepository.deleteById(uuid1));

        // Проверка, что запись удалена
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE id = ?")) {
            statement.setObject(1, uuid1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next());
            }
        }
    }

    @AfterAll
    static void tearDownContainer() {
        postgreSQLContainer.stop();
        postgreSQLContainer.close();
    }

}
